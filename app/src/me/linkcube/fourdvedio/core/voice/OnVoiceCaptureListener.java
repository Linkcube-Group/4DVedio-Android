package me.linkcube.fourdvedio.core.voice;

import me.linkcube.fourdvedio.FourDVedioApplication;
import me.linkcube.fourdvedio.utils.Timber;
import android.annotation.SuppressLint;
import android.media.audiofx.Visualizer;
import android.os.RemoteException;
import android.util.Log;

@SuppressLint("NewApi")
public class OnVoiceCaptureListener implements Visualizer.OnDataCaptureListener {

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

		Log.d("WaveEng:",""+ waveng);

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
