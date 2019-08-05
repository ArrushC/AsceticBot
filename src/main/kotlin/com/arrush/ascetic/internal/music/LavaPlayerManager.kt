package com.arrush.ascetic.internal.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrameBufferFactory
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer
import discord4j.voice.AudioProvider


class LavaPlayerManager {
    val playerManager: AudioPlayerManager = DefaultAudioPlayerManager()
    val player: AudioPlayer = playerManager.createPlayer()
    val provider: AudioProvider = AudioProviderImpl(player)

    init {
        // This is an optimization strategy that Discord4J can utilize. It is not important to understand
        playerManager.configuration.frameBufferFactory = AudioFrameBufferFactory { bufferDuration, format, stopping -> NonAllocatingAudioFrameBuffer(bufferDuration, format, stopping) }
        // Allow playerManager to parse remote sources like YouTube links
        AudioSourceManagers.registerRemoteSources(playerManager)
    }


    fun playMusic(url: String) {

    }
}