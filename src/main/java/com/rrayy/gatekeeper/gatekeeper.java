package com.rrayy.gatekeeper;

import org.bukkit.plugin.java.JavaPlugin;

import build.buf.gen.minekube.gate.v1.GateServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ManagedChannel;


public class gatekeeper extends JavaPlugin {
    private ManagedChannel channel;
    public GateServiceGrpc.GateServiceBlockingStub stub;

    @Override
    public void onEnable() { // Plugin startup logic
        getServer().getPluginManager().registerEvents(new event(this), this);
        try {
            channel = ManagedChannelBuilder // Create a gRPC channel
                .forAddress("localhost", 8080)
                .usePlaintext()
                .build();

            stub = GateServiceGrpc.newBlockingStub(channel);    // Create a blocking stub
        } catch (Exception e) {
            getLogger().severe("Make sure Gate is running with the API enabled");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        channel.shutdown(); // Shutdown the channel
        getLogger().info("TPA Plugin has been disabled!");
    }
}
