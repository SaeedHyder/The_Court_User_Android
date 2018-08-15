package com.app.court.retrofit;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressResponseBody extends ResponseBody {
    //the actual package to be wrapped
    private final ResponseBody responseBody;
    // Progress callback interface
    private final ProgressListener progressListener;
    // Package Completed BufferedSource
    private BufferedSource bufferedSource;

    /**
     * Constructor function
     *
     * @param responseBody     The response to be wrapped
     * @param progressListener callback interface
     */
    public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }


    /**
     * Rewrite the contentType that calls the actual response body
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    /**
     * Rewrite the contentLength that calls the actual response body
     *
     * @return contentLength
     * @throws IOException exception
     */
    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    /**
     * Rewrite the wrapper
     *
     * @return BufferedSource
     * @throws IOException exception
     */
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            //package
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    /**
     * Read, callback progress interface
     *
     * @param source Source
     * @return Source
     */
    private Source source(Source source) {

        return new ForwardingSource(source) {

            // the number of bytes currently read
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // increase the number of bytes currently read, if read is completed bytesRead will return -1
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                // callback if contentLength () does not know the length, it will return -1
                if (progressListener != null) {
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                }
                return bytesRead;
            }
        };
    }

    interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }
}
