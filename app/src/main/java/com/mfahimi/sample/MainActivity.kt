package com.mfahimi.sample

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val DirSeperator = "/"
    private val FileDirectoryOnAssets = "files"
    private val zipPassword = "123456"
    private val zipFileName = "myTextFile.zip"
    private val Zip_File_Content = "myTextFile.txt"
    private lateinit var outputFIleDirectory: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        outputFIleDirectory = "${Environment.getDataDirectory()}/data/${packageName}${DirSeperator}files$DirSeperator"
        Executors.newSingleThreadExecutor().execute {
            Unzipper.unZipFile(
                    application.assets.open(FileDirectoryOnAssets + DirSeperator + zipFileName),
                    outputFIleDirectory,
                    zipPassword
            )
            readFileContent()// read the content of the file to ensure unzip success
        }
    }

    private fun readFileContent() {
        try {
            val file = File("$outputFIleDirectory$Zip_File_Content")
            file.bufferedReader(Charsets.UTF_8).use {
                val text = it.readText()
                runOnUiThread {
                    tv_content.text = text
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}