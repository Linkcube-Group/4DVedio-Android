package me.linkcube.FourDVedio.core.voice;


import me.linkcube.FourDVedio.FourDVedioApplication;
import me.linkcube.FourDVedio.ui.listener.ASmackRequestCallBack;
import me.linkcube.FourDVedio.ui.listener.ModeControlListener;
import android.annotation.SuppressLint;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.RemoteException;
import android.util.Log;

@SuppressLint("NewApi")
public class VoiceSensor {

	private Visualizer mVisualizer = null;
	private OnVoiceCaptureListener voiceCaptureListener;
	private int captureRate;
	private ModeControlListener soundListener;

	public VoiceSensor() {

		if (Build.VERSION.SDK_INT >= 9) {
			captureRate = Visualizer.getMaxCaptureRate();
			mVisualizer = new Visualizer(0);
			mVisualizer.setCaptureSize(128);
			voiceCaptureListener = new OnVoiceCaptureListener();
			mVisualizer.setDataCaptureListener(voiceCaptureListener,
					captureRate / 2, false, true);
			mVisualizer.setEnabled(false);
		} 
	}

	public void setVoiceLevel(int level) {
		if (Build.VERSION.SDK_INT >= 9) {
			voiceCaptureListener.setLevel(level);
		} else {
			AudioRecorder.getInstance().setLevel(level);
			if(level>0){
				AudioRecorder.getInstance().startAudioRecorder(new ASmackRequestCallBack() {
					
					@Override
					public void responseSuccess(Object object) {
						
					}
					
					@Override
					public void responseFailure(int reflag) {
						
					}
				});
			}else{
				AudioRecorder.getInstance().stopAudioRecorder();
			}
		}
	}

	public void registerVoiceListener() {
		mVisualizer.setEnabled(true);

	}

	public void unregisterVoiceListener() {
		if (mVisualizer.getEnabled())
			mVisualizer.setEnabled(false);
	}
	
	class OnVoiceCaptureListener implements Visualizer.OnDataCaptureListener {

		public int level;

		private int count = 0;

		@Override
		public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform,
				int samplingRate) {
			long sound = VoiceUtils.computeWaveLevel(waveform) * level;
			try {
				FourDVedioApplication.toyServiceCall.setWave(sound);// 根据声音设置玩具速度
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			return;

		}

		@Override
		public void onFftDataCapture(Visualizer visualizer, byte[] fft,
				int samplingRate) {
			count++;
			count = count % 8;
			if (count == 1) {
				return;
			}
			long waveng = VoiceUtils.computeFFTLevel(fft) * level;

			try {
				FourDVedioApplication.toyServiceCall.setVoiceSensitivity(level);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}

			Log.d("WaveEng:",""+ waveng+"--level:"+level);
			soundListener.mobileVibrator((int) waveng);
			try {
				FourDVedioApplication.toyServiceCall.setWave(waveng);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			return;
		}

		public void setLevel(int level) {
			this.level = level;
		}
	}

	public void setSoundListener(ModeControlListener soundListener) {
		this.soundListener=soundListener;
	}

}
