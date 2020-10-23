package com.mfahimi.sample

import net.lingala.zip4j.ZipFile
import java.io.*
import java.lang.IllegalArgumentException

object Unzipper {
    private val zipFileName = "myZipFile"
    @Throws(IOException::class)
     fun unZipFile(originZipFile: InputStream,
                         outputFIleDirectory: String,
                         password: String? = null) {
        copyZipFileFromAssetsToDestinationFolder(originZipFile,outputFIleDirectory)
        val zipFilePath = "$outputFIleDirectory$zipFileName"
        val zipFile = ZipFile(zipFilePath)
        if (zipFile.isEncrypted) {
            if (password==null)
                throw IllegalArgumentException("zip file is encrypted and password must not be null")
            zipFile.setPassword(password.toCharArray())
        }
        zipFile.extractAll(outputFIleDirectory)
        val file = File(zipFilePath)
        if (file.exists()) file.delete()

    }

    @Throws(IOException::class)
    private fun copyZipFileFromAssetsToDestinationFolder(zipFile: InputStream,outputFIleDirectory: String) {
        var file = File(outputFIleDirectory)
        file.mkdirs()

        val myInputZipFile = zipFile
        val outputZipFile = "$outputFIleDirectory$zipFileName"

        file = File(outputZipFile)
        if (file.exists()) file.delete()
        val myOutput: OutputStream = FileOutputStream(outputZipFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (myInputZipFile.read(buffer).also { length = it } > 0) {
            myOutput.write(buffer, 0, length)
        }
        myOutput.flush()
        myOutput.close()
        myInputZipFile.close()
    }
}