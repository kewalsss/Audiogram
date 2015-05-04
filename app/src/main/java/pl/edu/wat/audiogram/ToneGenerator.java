package pl.edu.wat.audiogram;

import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import static android.media.AudioFormat.CHANNEL_OUT_MONO;
import static android.media.AudioFormat.ENCODING_PCM_16BIT;

class ToneGenerator  {
    private final static int SAMPLE_RATE = 48000;
    public static float calibration = 0;


    private AudioTrack audioTrack;
    private AudioTrackListener audioTrackListener;
    //konstruktor
    public ToneGenerator() {
    }

    public ToneGenerator(AudioTrackListener audioTrackListener) {
        this.audioTrackListener = audioTrackListener;

    }

    //odtwarzanie tonów
    public void playCalibration() {
        short[] generatedCalibration = generateCalibration();
        play(generatedCalibration, true);
    }

    public void playTone(double frequency, double amplitude, int duration) {
        short[] generatedTone = generateTone(frequency, amplitude, duration);
        play(generatedTone, false);
    }

    private void play(short[] sample, boolean loop) {
        //!!! odtwórz żądane próbki
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                SAMPLE_RATE, CHANNEL_OUT_MONO,
                ENCODING_PCM_16BIT, sample.length * 2,
                AudioTrack.MODE_STATIC);
        audioTrack.write(sample, 0, sample.length);
        if (loop) {
            audioTrack.setLoopPoints(0, sample.length, -1);
        }

        if (audioTrackListener != null && !loop) {
            audioTrack.setNotificationMarkerPosition(sample.length);
            audioTrack.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
                @Override
                public void onPeriodicNotification(AudioTrack track) {
                }
                @Override
                public void onMarkerReached(AudioTrack track) {
                audioTrackListener.onFinishPlay();
                }
            });
        }

        audioTrack.play();

    }

    public void stop() {
        try {
            audioTrack.stop();
            audioTrack.release();
        } catch (Exception e) {
            Log.d("ToneGenerator", "exception captured");
        }
    }

    //generowanie tonów
    private short[] generateTone(double frequency, double amplitude, int duration) {

        // liczba próbek
        int numSamples = duration * SAMPLE_RATE;
        // tablica przechowująca próbki (short zajmuje 16 bitów)
        short sample[] = new short[numSamples];
        // tworzenie próbek
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = (short) (amplitude * Math.sin(2 * Math.PI * i /
                    (SAMPLE_RATE / frequency)) * Short.MAX_VALUE);
        }
        return sample;
    }

    private short[] generateCalibration() {
        //tworzymy ton 1000Hz
        short firstTone[] = generateTone(1000, gain(0), 1);
        short secondTone[] = generateTone(1000, gain(-5), 1);

        //łączymy te tony w nową tablicę
        short sound[] = new short[firstTone.length + secondTone.length];
        //kopiujemy do nowej tablicy
        System.arraycopy(firstTone, 0, sound, 0, firstTone.length);
        System.arraycopy(secondTone, 0, sound, firstTone.length, secondTone.length);
        return sound;
    }

    //sterowanie poziomem głośności
    public void setCalibrationVolume(float gain) {
        audioTrack.setStereoVolume(gain, gain);
    }

    public void volume(float leftVolume, float rightVolume) {
        audioTrack.setStereoVolume(leftVolume, rightVolume);
    }

    //metody pomocnicze

    /**
     * Zamiana db w wartość liniową
     *
     * @param db wartość w dB
     * @return wartość liniowa
     */
    public double gain(double db) {
        return Math.pow(10, db / 20);
    }

    /**
     * Wylicza skalibrowaną amplitudę z podanej wartości
     * @param db wzmocnienia w dB
     * @return amplituda
     */
    public double amplitude(int db) {
        return calibration * gain(db);
    }


}
