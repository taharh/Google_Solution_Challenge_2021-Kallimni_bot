package com.asynctaskcoffee.audiorecorder.worker

import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import java.io.File
import java.io.IOException
import java.util.*

class Recorder(audioRecordListener: AudioRecordListener?) {

    private var recorder: MediaRecorder? = null
    private var audioRecordListener: AudioRecordListener? = null
    private var fileName: String? = null
    private var localPath = ""

    private var isRecording = false

    fun setFileName(fileName: String?) {
        this.fileName = fileName
    }

    private fun getAbsoluteFile(context: Context): File? {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            context.getExternalFilesDir(null)
        } else {
            context.filesDir
        }
    }

    fun startRecord(context: Context) {
        recorder = MediaRecorder()
        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//        localPath = Environment.getExternalStorageDirectory().absolutePath
        localPath = getAbsoluteFile(context)!!.absolutePath
        localPath += if (fileName == null) {
            "/Recorder_" + UUID.randomUUID().toString() + "hamza.m4a"
        } else {
            fileName
        }
        recorder!!.setOutputFile(localPath)
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        try {
            recorder!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
            reflectError(e.toString())
            return
        }
        recorder!!.start()
        isRecording = true
    }

    fun reset() {
        if (recorder != null) {
            recorder!!.release()
            recorder = null
            isRecording = false
        }
    }

    fun stopRecording() {
        try {
            Thread.sleep(150)
            recorder!!.stop()
            recorder!!.release()
            recorder = null
            reflectRecord(localPath)
        } catch (e: Exception) {
            e.printStackTrace()
            reflectError(e.toString())
        }
    }

    private fun reflectError(error: String?) {
        audioRecordListener?.onRecordFailed(error)
        isRecording = false
    }

    private fun reflectRecord(uri: String?) {
        audioRecordListener?.onAudioReady(uri)
        isRecording = false
    }

    init {
        this.audioRecordListener = audioRecordListener
    }
}