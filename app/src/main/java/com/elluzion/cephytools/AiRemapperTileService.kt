package com.elluzion.cephytools

import android.content.Intent
import android.service.quicksettings.TileService

class AiRemapperTileService: TileService(){

    override fun onClick() {
        super.onClick()

        val intent = Intent(this, AIButtonRemapperActivity::class.java).apply {}
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivityAndCollapse(intent)
    }

    override fun onTileRemoved() {
        super.onTileRemoved()

        // Do something when the user removes the Tile
    }

    override fun onTileAdded() {
        super.onTileAdded()

        // Do something when the user add the Tile
    }

    override fun onStartListening() {
        super.onStartListening()

        // Called when the Tile becomes visible
    }

    override fun onStopListening() {
        super.onStopListening()

        // Called when the tile is no longer visible
    }
}