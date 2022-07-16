package scrapscramble.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.rest.RestClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class DiscordBotApplication {

    public static void main(String[] args) {
        //Start spring application
        new SpringApplicationBuilder(DiscordBotApplication.class)
                .build()
                .run(args);
    }


    @Bean
    public GatewayDiscordClient gatewayDiscordClient() throws IOException {
        ObjectMapper om = new ObjectMapper();
        Configurations configs = om.readValue(new File("config.json"), Configurations.class);
        return DiscordClientBuilder.create(configs.getToken()).build()
                .gateway()
                .setInitialPresence(ignore -> ClientPresence.online(ClientActivity.playing("Scrap Scramble")))
                .login()
                .block();
    }

    @Bean
    public RestClient discordRestClient(GatewayDiscordClient client) {
        return client.getRestClient();
    }
}
