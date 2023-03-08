package com.github.tanokun.tanorpg.util.io

import org.bukkit.plugin.Plugin
import java.io.File

class Folder(val plugin: Plugin, val file: File, val folderName: String) {

    /**
     * dataFolderを作成します
     * @param plugin Pluginのインスタンス
     */
    constructor(plugin: Plugin): this(plugin, plugin.dataFolder, plugin.name)

    /**
     * 任意の名前のdataFolderを作成します
     * @param plugin Pluginのインスタンス
     * @param folderName 任意のフォルダ名
     */
    constructor(plugin: Plugin, folderName: String): this(plugin, File(plugin.dataFolder, folderName), folderName)

    /**
     * フォルダ内のファイルをコンフィグとして返します
     * @return フォルダ内の全ファイル
     */
    fun getFiles(): List<Config> {
        val configs: MutableList<Config> = ArrayList()

        for (file in file.listFiles()) {
            val config = Config(plugin, folderName + File.separator + file.name)
            config.fileName = file.name
            configs.add(config)
        }
        return configs
    }
}