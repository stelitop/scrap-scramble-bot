package scrapscramble.game;

import org.apache.commons.lang3.RandomUtils;
import scrapscramble.game.cards.CardPool;
import scrapscramble.game.cards.StatusKeyword;
import scrapscramble.game.cards.effects.EffectCaller;
import scrapscramble.game.cards.effects.EffectTrigger;
import scrapscramble.game.cards.effects.context.EffectContext;
import scrapscramble.game.cards.effects.context.StartOfCombatContext;
import scrapscramble.game.player.PairMaker;
import scrapscramble.game.player.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
    /**
     * Object containing all data about the settings of the game.
     */
    private final GameSettings settings;

    /**
     * The starting card pool for all players in the game.
     */
    private CardPool cardPool;

    /**
     * The players participating in the game.
     */
    private List<Player> players;

    /**
     * The current round of the game
     */
    private int round;

    /**
     * Whether the game has started or not.
     */
    private boolean hasStarted;
    /**
     * The pairmaker object that takes care of assigning fights.
     */
    private PairMaker pairMaker;
    /**
     * General effect caller used across the game.
     */
    private final EffectCaller effectCaller;

    /**
     * Default constructor. Creates a new game with default settings and
     * no players in it. Uses a default effect caller.
     */
    public Game() {
        this(new EffectCaller());
    }

    /**
     * Creates a new game with a custom effect caller. Primarily used for DI.
     * @param effectCaller Effect caller.
     */
    public Game(EffectCaller effectCaller) {
        this.settings = new GameSettings();
        this.cardPool = new CardPool();
        this.players = new ArrayList<>();
        this.hasStarted = false;
        this.pairMaker = null;
        this.effectCaller = effectCaller;
        this.round = 1;
    }

    /**
     * Gets the settings of the game.
     * @return The settings of the game
     */
    public GameSettings getSettings() {
        return this.settings;
    }

    /**
     * Adds a new player to the game. The game object and settings should
     * have already been prepared for starting a game prior to adding new
     * players.
     * @param name The name of the player.
     * @return A reference to the new Player object.
     */
    public Player addPlayer(String name) {
        Player player = new Player(this, name);
        this.players.add(player);
        return player;
    }

    /**
     * Gets the card pool used for the game.
     * @return Card Pool.
     */
    public CardPool getCardPool() {
        return this.cardPool;
    }

    /**
     * Gets the effect caller for this game.
     * @return Effect caller.
     */
    public EffectCaller getEffectCaller() {
        return this.effectCaller;
    }

    /**
     * Gets whether the game has started or not.
     * @return True if there's an ongoing game using this object, false otherwise.
     */
    public boolean hasStarted() {
        return this.hasStarted;
    }

    /**
     * Starts a new game.
     * @param players Amount of players in the game.
     * @param names A list of the names of all players. They must be all
     *              different
     * @param cardPool The card pool used by the game.
     * @throws IllegalArgumentException If there are any duplicates in the
     * names list.
     */
    public void start(final int players, List<String> names, CardPool cardPool) throws IllegalArgumentException{
        if (names.stream().distinct().count() != (long)names.size()) {
            throw new IllegalArgumentException("There are duplicate names in the list!");
        }
        this.hasStarted = true;
        this.cardPool = cardPool;
        this.players.clear();
        this.round = 1;

        for (int i = 0; i < players; i++) {
            Player player = this.addPlayer(names.get(i));
            player.getCreatureData().setAttack(1);
            player.getCreatureData().setHealth(1);
            player.getShop().refresh(this, player, true);
        }

        this.pairMaker = new PairMaker(this.players);
        this.pairMaker.generateNextTurnPairings();
    }

    /**
     * Gets a list of all players.
     * @return A list of all players. Changes to this list are not reflected on
     * the game object, however changes to the players themselves are.
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(this.players);
    }

    /**
     * Proceeds to the next round of the game. The game should have already started
     * before calling this.
     * @throws IllegalArgumentException If the game hasn't started yet.
     */
    public void nextRound() throws IllegalStateException {
        if (!this.hasStarted()) throw new IllegalStateException("The game has not started. Unable to call nextRound()");
        this.round++;
        this.pairMaker.generateNextTurnPairings();

        this.players.forEach(player -> {
            player.clearAftermathMessages();
            // increase maximum mana
            player.setMaximumMana(Integer.min(
                    player.getMaximumMana() + 5, player.getMaximumManaCap()
            ));
            player.setCurrentMana(player.getMaximumMana());
            // refresh shop
            player.getShop().refresh(this, player, true);
            // transfer overload
            player.setOverloadedMana(player.getCreatureData().getStatusKeyword(StatusKeyword.Overload));
            player.setCurrentMana(player.getCurrentMana() - player.getOverloadedMana());
            // add a new layer to the histories
            player.getBuyHistory().createLayer();
            player.getPlayHistory().createLayer();
            // clear keywords
            player.getCreatureData().clearStatusKeywords();
        });

        this.players.forEach(player -> { // trigger AftermathPlayer
            this.effectCaller.activate(player.getEffects(), new EffectContext(EffectTrigger.AftermathPlayer, this, player, null));
        });
        this.players.forEach(player -> { // trigger AftermathOpponent
            this.effectCaller.activate(player.getEffects(), new EffectContext(EffectTrigger.AftermathOpponent  , this, player, null));
        });

        // gain the effects for next turn
        this.players.forEach(Player::gainNextRoundEffects);
        // create a new layer for the attached upgrades
        this.players.forEach(player -> {
           player.getAttachedUpgrades().createLayer();
        });
    }

    /**
     * Gets the opponent of a given player. This refers the call to the
     * pairmaker object.
     * @param player The player for whose opponent to look for.
     * @return The opponent of the player if there is one. If the player
     * gets a bye, a reference o the same player object. If the player is
     * not a part of the game null is returned.
     */
    public Player getOpponent(Player player) {
        return this.pairMaker.getOpponent(player);
    }

    /**
     * Gets which round the game is at currently.
     * @return The game round.
     */
    public int getRound() {
        return this.round;
    }

    /**
     * Conducts all fights between all current pairs in the game.
     * @return A list of outputs for each fight that happened. Dead players
     * or players with a bye are not represented in this list.
     */
    public List<FightOutput> conductFights() {
        List<FightOutput> ret = new ArrayList<>();
        Set<Player> fought = new HashSet<>();
        for (var player : players) {
            if (fought.contains(player)) continue;
            Player opponent = pairMaker.getOpponent(player);
            if (opponent != null && opponent != player) {
                ret.add(fight(player, opponent));
                fought.add(player);
                fought.add(opponent);
            }
        }
        return ret;
    }

    /**
     * Conducts a fight between two players. Both players should be a part of
     * this game object. The loser of the fight loses a life.
     * @param p1 Player 1.
     * @param p2 Player 2.
     * @return A fight output object that contains human-readable information
     * about everything that happened during the fight.
     */
    public FightOutput fight(Player p1, Player p2) {
        // check if the player is not in the game object
        if (!this.players.contains(p1) || !this.players.contains(p2)) return null;
        FightOutput fightOutput = new FightOutput(p1, p2);
        // TODO set destroyed to false
        // write the lists of ugprades of each player
        for (var u : p1.getAttachedUpgrades().getLastLayer()) {
            fightOutput.addMessage(FightOutput.Location.Player1Upgrades, u.getName());
        }
        for (var u : p2.getAttachedUpgrades().getLastLayer()) {
            fightOutput.addMessage(FightOutput.Location.Player2Upgrades, u.getName());
        }
        // TODO write the lists of effects of each player

        // remember the attack and health to restore them at the end of the fight
        int remAttack1 = p1.getAttack(), remAttack2 = p2.getAttack();
        int remHealth1 = p1.getHealth(), remHealth2 = p2.getHealth();

        // determine who goes first. default is p1 goes first
        // only check if it should be the other way around
        Player firstPlayer = p1, secondPlayer = p2;
        boolean isCoinflip = false;

        if (p1.getPriorityScore() < p2.getPriorityScore()) {
            firstPlayer = p2;
            secondPlayer = p1;
        } else if (p1.getPriorityScore() == p2.getPriorityScore()) {
            if (p1.getCreatureData().getStatusKeyword(StatusKeyword.Tiebreaker) < p2.getCreatureData().getStatusKeyword(StatusKeyword.Tiebreaker)) {
                firstPlayer = p2;
                secondPlayer = p1;
            } else if (p1.getCreatureData().getStatusKeyword(StatusKeyword.Tiebreaker) == p2.getCreatureData().getStatusKeyword(StatusKeyword.Tiebreaker)) {
                isCoinflip = true;
                if (RandomUtils.nextInt(0, 2) == 1) {
                    firstPlayer = p2;
                    secondPlayer = p1;
                }
            }
        }

        // append message
        if (!isCoinflip) fightOutput.addMessage(FightOutput.Location.BeforeCombat, firstPlayer.getName() + " has Attack Priority.");
        else fightOutput.addMessage(FightOutput.Location.BeforeCombat,firstPlayer.getName() + " wins the coinflip for Attack Priority.");

        // TODO check for room trickers

        // trigger start of combat effects
        effectCaller.activate(firstPlayer.getEffects(), new StartOfCombatContext(
            this, firstPlayer, fightOutput));
        effectCaller.activate(secondPlayer.getEffects(), new StartOfCombatContext(
                this, secondPlayer, fightOutput));

        // combat starts
        for (int currentTurn = 0; p1.isAlive() && p2.isAlive(); currentTurn++) {
            // determine attacker and defender, changes each turn
            Player attacker = firstPlayer, defender = secondPlayer;
            if (currentTurn % 2 == 1) {
                attacker = secondPlayer;
                defender = firstPlayer;
            }
            System.out.println(defender.getHealth());
            attacker.attackPlayer(this, defender, fightOutput);
            if (!attacker.isAlive() || !defender.isAlive()) break;
            // TODO call after this attacks effects for attacker
            //if (!attacker.isAlive() || !defender.isAlive()) break;
            // TODO call after the enemy attacks effects for defender
        }

        // write about the winner
        Player winner = p2, loser = p2;
        if (p1.isAlive()) winner = p1;
        else loser = p1;

        fightOutput.addMessage(FightOutput.Location.DuringCombat, winner.getName() + " has won!");
        loser.decreaseLives();
        // TODO register results in the pair maker
        // revert player's stats
        p1.setAttack(remAttack1);
        p1.setHealth(remHealth1);
        p2.setAttack(remAttack2);
        p2.setHealth(remHealth2);

        return fightOutput;
    }
}
