package org.androidtown.Floremo.TodayFlower;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

        /*MediaScanner가 필요한 이유*/

        /*Android에서 사진을 찍었는데 갤러리에 바로 사진이 보이지 않을 경우나
        파일을 생성하였는데 파일 관리자에서 보이지 않을 경우가 존재할 수 있다.
        이때 안드로이드를 재부팅하거나 SD카드를 재창가하지 않고
        수동으로 미디어 스캔을 해주는 클래스이다.*/

public class MediaScanner {
    private Context ctxt;
    private String file_Path;
    private MediaScannerConnection mMediaScanner;
    private MediaScannerConnection.MediaScannerConnectionClient mMediaScannerClient;
    private Context mContext;
    private String mPath;

    public static MediaScanner newInstance(Context context) {
        return new MediaScanner (context);
    }

    private MediaScanner (Context context) {
        ctxt = context;
    }

    public void mediaScanning(final String path) {
        if (mMediaScanner == null) {
            mMediaScannerClient = new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override public void onMediaScannerConnected() {
                    mMediaScanner.scanFile(file_Path, null);
                }

                @Override public void onScanCompleted(String path, Uri uri) {
                    System.out.println("::::MediaScan Success::::");

                    mMediaScanner.disconnect();
                }
            };

            mMediaScanner = new MediaScannerConnection(mContext, mMediaScannerClient);
        }

        mPath = path;
        mMediaScanner.connect();
    }


}
