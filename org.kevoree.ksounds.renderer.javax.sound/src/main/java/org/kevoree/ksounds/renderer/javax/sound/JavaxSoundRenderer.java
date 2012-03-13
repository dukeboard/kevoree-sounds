package org.kevoree.ksounds.renderer.javax.sound;

import org.kevoree.framework.message.StdKevoreeMessage;
import org.kevoree.ksounds.api.AudioRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;

/**
 * User: Erwan Daubert - erwan.daubert@gmail.com
 * Date: 13/03/12
 * Time: 21:13
 *
 * @author Erwan Daubert
 * @version 1.0
 */
public class JavaxSoundRenderer extends AudioRenderer {


	public static final int BUFFER_SIZE = (int) (Math.pow(2, 15) / 24) * 24;
	private final Logger logger = LoggerFactory.getLogger(getClass().getName());
	private SourceDataLine line;
	private FloatControl volumeControl;
	private boolean mixerChanged;
	private Mixer mixer;
	private float volume = 1f;
	private boolean linearVolume = false;

	@Override
	public void configureFormat (Object message) {
		try {
			//check the format message
			if (message instanceof StdKevoreeMessage) {
				StdKevoreeMessage stdMessage = (StdKevoreeMessage)message;
				//build AudioFormat according to format + Encoding.PCM_UNSIGNED
				float samplerate = (float)stdMessage.getValue("samplerate").getOrElse(AudioSystem.NOT_SPECIFIED);
				int bitrate = (int)stdMessage.getValue("samplerate").getOrElse(AudioSystem.NOT_SPECIFIED);
				int channels = (int)stdMessage.getValue("samplerate").getOrElse(AudioSystem.NOT_SPECIFIED);
				AudioFormat fmt = new AudioFormat(samplerate, bitrate, channels, true, false);
				if (line != null && line.isOpen()) {
					if (mixerChanged || !line.getFormat().matches(fmt)) {
						mixerChanged = false;
						line.drain();
						line.close();
						line = null;
					} else {
						return;
					}
				}
				logger.debug("Audio format: " + fmt);
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, fmt, BUFFER_SIZE);
				logger.debug("Dataline info: " + info);
				if (mixer != null && mixer.isLineSupported(info)) {
					line = (SourceDataLine) mixer.getLine(info);
					logger.debug("Mixer: " + mixer.getMixerInfo().getDescription());
				} else {
					line = AudioSystem.getSourceDataLine(fmt);
					mixer = null;
				}
				logger.debug("Line: " + line);
				line.open(fmt, BUFFER_SIZE);
				line.start();
				if (line.isControlSupported(FloatControl.Type.VOLUME)) {
					volumeControl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
					volumeControl.setValue(volume * volumeControl.getMaximum());
					linearVolume = true;
				} else if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
					volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
					volumeControl.setValue(linearToDb(volume));
					linearVolume = false;
				}
			}
		} catch (LineUnavailableException e) {
			logger.error("Unable to get a line to send sounds", e);
		}
	}

	@Override
	public void writeData (Object message) {
		// enqueue data to be write on the line
		if (message instanceof StdKevoreeMessage) {
			byte[] buf = ((StdKevoreeMessage)message).getValue("data").getOrElse(new byte[0]);
			int length = ((StdKevoreeMessage)message).getValue("length").getOrElse(buf.length);
			line.write(buf, 0, length);
		}
	}

	public void setVolume (float volume) {
		this.volume = volume;
		if (volumeControl != null) {
			if (linearVolume) {
				volumeControl.setValue(volumeControl.getMaximum() * volume);
			} else {
				volumeControl.setValue(linearToDb(volume));
			}
		}
	}

	public float getVolume (boolean actual) {
		if (actual && volumeControl != null) {
			if (linearVolume) {
				return this.volumeControl.getValue() / volumeControl.getMaximum();
			} else {
				return dbToLinear(volumeControl.getValue());
			}
		} else {
			return volume;
		}
	}

	private float linearToDb (double volume) {
		return (float) (20 * Math.log10(volume));
	}

	private float dbToLinear (double volume) {
		return (float) Math.pow(10, volume / 20);
	}

}
