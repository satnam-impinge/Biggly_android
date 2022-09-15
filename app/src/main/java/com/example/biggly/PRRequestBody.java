package com.example.biggly;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class PRRequestBody extends RequestBody {
        private File mFile;

        int mIndex;
    FileUploadercallback mProgressUpdatesListenr;
        private static final int DEFAULT_BUFFER_SIZE = 2048;
    public PRRequestBody(final File file) {
        mFile = file;


    }

        public PRRequestBody(final File file,int index, FileUploadercallback progressUpdater) {
            mFile = file;
            mIndex = index;
            mProgressUpdatesListenr = progressUpdater;

        }

        @Override
        public MediaType contentType() {
            // i want to upload only images
            return MediaType.parse("image/*");
        }

        @Override
        public long contentLength() throws IOException {
            return mFile.length();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            long fileLength = mFile.length();
            Context context;
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            FileInputStream in = new FileInputStream(mFile);


            long uploaded = 0;

            try {
                int read;
                Handler handler = new Handler(Looper.getMainLooper());
                while ((read = in.read(buffer)) != -1) {

                    // update progress on UI thread
                    handler.post(new ProgressUpdater(uploaded, fileLength));
                    uploaded += read;
                    sink.write(buffer, 0, read);
                }
            } finally {
                in.close();
            }
        }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;
        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            int current_percent = (int)(100 * mUploaded / mTotal);

            Log.i("percecent",current_percent+" ---------------");
//            int total_percent = (int)(100 * (totalFileUploaded+mUploaded) / totalFileLength);

            if(mProgressUpdatesListenr != null)
              mProgressUpdatesListenr.onProgressUpdate(current_percent, current_percent,mIndex );

        }
    }

    public interface FileUploadercallback{
            public void onProgressUpdate(int current_percent,int totalPercent,int index);
            public void onFailure();
    }

}
