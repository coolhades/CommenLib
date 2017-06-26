package com.hades.cloudlib.play;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.bokecc.sdk.mobile.play.DWMediaPlayer;
import com.hades.cclibmanager.CCdrmServerManager;

import java.io.IOException;


public class VideoService extends Service {

    private static final String TAG = "VideoService";

    private IBinder binder = new ControlBind();
    private SurfaceHolder surfaceHolder;
    private DWMediaPlayer player;


    private int currentPosition = -1;

    //MediaPlayer 状态
    private static final int MODE_IDLE = 0;
    private static final int MODE_INITIALIZED = 1;
    private static final int MODE_PREPARED = 2;
    private static final int MODE_STARTED = 3;
    private static final int MODE_PAUSED = 4;
    private static final int MODE_STOPPED = 5;

    private int MEDIA_PLAYER_MODE;

    private void setMode(int status){
        MEDIA_PLAYER_MODE = status;
    }

    SurfaceHolder.Callback holderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            if (null != player) {
                player.setDisplay(surfaceHolder);
                updataPorgress();
            }
            //继续播放【Activity走onResume】


        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            // 销毁SurfaceHolder的时候记录当前的播放位置并停止播放
            if (player != null && player.isPlaying()) {
                currentPosition = player.getCurrentPosition();
                player.stop();
                player.reset();
                player.release();
                player = null;
            }
            surfaceHolder.getSurface().release();
        }
    };

    IVideoCallBack mVideoCallBack;
    private boolean isPlaying = false;

    /**
     * 创建时间 2017/5/4
     * auther Hades
     * 描述 回调注入
     **/
    public void setVideoCallBack(IVideoCallBack videoCallBack) {
        this.mVideoCallBack = videoCallBack;
    }

    public void removeCallBack(){

    }

    /**
     * 创建时间 2017/5/4
     * auther Hades
     * 描述 bind/rebind surfaceview
     * onResume 后需要重绑
     **/
    public void bindSurfaceView(SurfaceView surfaceView) {
        surfaceHolder = surfaceView.getHolder();//SurfaceHolder是SurfaceView的控制接口
        surfaceHolder.addCallback(holderCallback);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//Surface类型
    }

    /**
    * 创建时间 2017/5/4
    * auther Hades
    * 描述 释放资源
    **/
    public void onDestory(){
        surfaceHolder.getSurface().release();
        surfaceHolder = null;
    }

    /**
     * 创建时间 2017/5/4
     * auther Hades
     * 描述 设置视频资源
     **/
    public void setVideData(VideoTaskBean info) {
        if (null == info.getItem_datas().getVideo().getVideo_value()
                || info.getItem_datas().getVideo().getVideo_value().isEmpty()){
            mVideoCallBack.onError();
            return;
        }
        setPlayer(info);
    }

    /**
     * 创建时间 2017/5/4
     * auther Hades
     * 描述 进度
     **/
    public void seekTo(int pos) {
        if (null == player) return;
        player.seekTo(pos);
    }

    /**
     * 创建时间 2017/5/4
     * auther Hades
     * 描述 播放按钮
     **/
    public void startPlay() {
        if (null == player) return;

        if (isPlaying) {
            mVideoCallBack.onPlayStatus(false);
            player.pause();
            setMode(MODE_PAUSED);
            currentPosition = player.getCurrentPosition();
            isPlaying = false;

        } else {
            mVideoCallBack.onPlayStatus(true);
            player.start();
            setMode(MODE_STARTED);
            isPlaying = true;
            //更新时间
            updataPorgress();
        }
    }

    public void stopPlay() {
        if (null == player) return;

        mVideoCallBack.onPlayStatus(false);
        player.pause();
        setMode(MODE_PAUSED);
        currentPosition = player.getCurrentPosition();
        isPlaying = false;

    }

    public void rePlay(){
        if (null == player) return;

        player.seekTo(0);
        mVideoCallBack.onPlayStatus(true);
        player.start();
        setMode(MODE_STARTED);
        isPlaying = true;
        //更新时间
        updataPorgress();

    }

    //切换任务类型时调用
    public void releasePlayer(){
        if (null == player)return;
        mVideoCallBack.onPlayStatus(false);
        currentPosition = player.getCurrentPosition();
        isPlaying = false;
        player.stop();
    }


    /**
     * 创建时间 2017/5/4
     * auther Hades
     * 描述 1s调用1次
     **/
    private void updataTime(int currentPosition) {
        if (null == mVideoCallBack) return;
        mVideoCallBack.onTimeUpdata(currentPosition);
    }


    private void setPlayer(VideoTaskBean info) {

        //初始化player
        initPlayer(info);
        player.setDRMServerPort(CCdrmServerManager.getInstance().getDrmServerPort());
        try {
            if (info.getItem_datas().getVideo().getVideo_type().equalsIgnoreCase("cc")) {
                if (info.getItem_datas().getVideo().getVideo_config().equalsIgnoreCase("admin")) {
                    player.setVideoPlayInfo(info.getItem_datas().getVideo().getVideo_value(), CCdrmServerManager.getInstance().getCC_Account_id(), CCdrmServerManager.getInstance().getCC_Account_Key(), this);
                } else {
                    player.setVideoPlayInfo(info.getItem_datas().getVideo().getVideo_value(), CCdrmServerManager.getInstance().getCC_Account_NO_id(), CCdrmServerManager.getInstance().getCC_Account_NO_Key(), this);
                }
            } else {
                player.setDataSource(info.getItem_datas().getVideo().getVideo_value());
            }
            setMode(MODE_INITIALIZED);
            CCdrmServerManager.getInstance().getDRMServer().reset();//cc SDK2.7.2解决了视频切换卡死问题
            player.prepareAsync();
        } catch (IllegalArgumentException e) {
            Log.e("player error", e.getMessage());
        } catch (SecurityException e) {
            Log.e("player error", e.getMessage());
        } catch (IllegalStateException e) {
            Log.e("player error", "illegal", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initPlayer(final VideoTaskBean info) {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }

        player = new DWMediaPlayer();
        player.reset();
        setMode(MODE_IDLE);
        player.setDisplay(surfaceHolder);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setScreenOnWhilePlaying(true);
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                setMode(MODE_PREPARED);
                //准备完毕 可以播放了
                if (info.getUser_act().getPass_status().equalsIgnoreCase("1")) {
                    //未看完
                    currentPosition = Float.valueOf(info.getUser_act().getSection_watch_time()).intValue();
                    player.seekTo(currentPosition);
//                    number_progress_bar.setProgress(currentPosition);
                } else {
                    currentPosition = 0;
//                    number_progress_bar.setProgress(0);
                }
                //controlbar 更新UI 控制按钮、进度条、时间
                mVideoCallBack.onTotalTime(player.getDuration());//更新总时间、总进度量
                mVideoCallBack.onContinue(currentPosition);//历史播放进度【未看完】
                mVideoCallBack.onPlayStatus(true);//控制播放按钮状态
                mediaPlayer.start();
                mVideoCallBack.onStartPlay();
                isPlaying = true;
                //更新进度
                updataPorgress();

            }
        });


        //播放完成
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //处理自动切换
                if (null == mVideoCallBack) return;

                if (checkCompletionStatus()) {
                    mVideoCallBack.onCompletion();//回调 播放完成
                }

            }
        });
        //进度拖放
        player.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            public void onSeekComplete(MediaPlayer m) {
                m.start();
            }
        });

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                Log.d(TAG, "OnError - Error code: " + what + " Extra code: " + extra);
                switch (what) {
                    case -1004:
                        Log.d(TAG, "MEDIA_ERROR_IO");
                        break;
                    case -1007:
                        Log.d(TAG, "MEDIA_ERROR_MALFORMED");
                        break;
                    case 200:
                        Log.d(TAG, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK");
                        break;
                    case 100:
                        Log.d(TAG, "MEDIA_ERROR_SERVER_DIED");
                        break;
                    case -110:
                        Log.d(TAG, "MEDIA_ERROR_TIMED_OUT");
                        break;
                    case 1:
                        Log.d(TAG, "MEDIA_ERROR_UNKNOWN");
                        break;
                    case -1010:
                        Log.d(TAG, "MEDIA_ERROR_UNSUPPORTED");
                        break;
                }
                switch (extra) {
                    case 800:
                        Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING");
                        break;
                    case 702:
                        Log.d(TAG, "MEDIA_INFO_BUFFERING_END");
                        break;
                    case 701:
                        Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE");
                        break;
                    case 802:
                        Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE");
                        break;
                    case 801:
                        Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE");
                        break;
                    case 1:
                        Log.d(TAG, "MEDIA_INFO_UNKNOWN");
                        break;
                    case 3:
                        Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START");
                        break;
                    case 700:
                        Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING");
                        break;
                }
                return false;
            }
        });

        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                //更新缓冲
            }
        });

        player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case DWMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        if (player.isPlaying()) {
                            mVideoCallBack.onBuffering(true);
                        }
                        break;
                    case DWMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        mVideoCallBack.onBuffering(false);
                        break;
                }
                return false;
            }
        });
    }

    private boolean checkCompletionStatus() {
        return !player.isPlaying() &&
                (MEDIA_PLAYER_MODE == MODE_STARTED
//                        || MEDIA_PLAYER_MODE == MODE_PAUSED
                        //播放完成后Player状态 MODE_PREPARED
                        || MEDIA_PLAYER_MODE == MODE_PREPARED);
    }

    private void updataPorgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isPlaying && null != player) {
                    try {
                        Thread.sleep(1000);//1s更新一次
                        try {
                            isPlaying = player.isPlaying();
                            updataTime(player.getCurrentPosition());
                        }
                        catch (IllegalStateException e) {
                            //切换过程会出现 状态错误
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public interface IVideoCallBack {
        void onCompletion();//播放完成

        void onBuffering(boolean isBuffering);

        void onTimeUpdata(int currentPos);//更新时间及进度

        void onTotalTime(int total);//play准备

        void onError();//播放出错

        void onPlayStatus(boolean isPlaying);

        void onContinue(int recordPos);//resume后继续播放

        void onStartPlay();
    }


    public VideoService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isPlaying = false;
        super.onDestroy();
    }

    public class ControlBind extends Binder {
        public VideoService getService() {
            return VideoService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {


        return super.onUnbind(intent);


    }
}
