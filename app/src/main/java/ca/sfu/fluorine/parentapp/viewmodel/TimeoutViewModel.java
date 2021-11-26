package ca.sfu.fluorine.parentapp.viewmodel;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ca.sfu.fluorine.parentapp.store.TimeoutStorage;

public class TimeoutViewModel extends AndroidViewModel {
    private final TimeoutStorage storage;

    // Constants
    private final long INTERVAL = 100;
    public enum TimeoutState {RUNNING, PAUSED, EXPIRED}

    // Live data
    private long totalDuration = 0;
    private final MutableLiveData<Long> millisLeft = new MutableLiveData<>(0L);
    private final MutableLiveData<TimeoutState> timerState
            = new MutableLiveData<>(TimeoutState.PAUSED);

    private CountDownTimer timer;

    public TimeoutViewModel(@NonNull Application application) {
        super(application);
        storage = TimeoutStorage.getInstance(application);
    }

    public LiveData<Long> getMillisLeft() {
        return millisLeft;
    }

    public LiveData<TimeoutState> getTimerState() {
        return timerState;
    }

    public void loadTimerFromStorage() {
        if (!storage.hasTimer()) {
            timerState.setValue(TimeoutState.EXPIRED);
            return;
        }
        timerState.setValue(TimeoutState.PAUSED);
        totalDuration = storage.getTotalDuration();
        makeTimerFromSettings();
        if (storage.isTimerRunning()) {
            timerState.setValue(TimeoutState.RUNNING);
            timer.start();
        }
    }

    public void resumeTimer() {
        makeTimerFromSettings();
        timer.start();
        timerState.setValue(TimeoutState.RUNNING);
    }

    public void pauseTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timerState.setValue(TimeoutState.PAUSED);
    }

    private void makeTimerFromSettings() {
        long duration = storage.calculateRemainingMillis();
        millisLeft.setValue(duration);
        timer = new CountDownTimer(duration, INTERVAL) {
            @Override
            public void onTick(long remaining) {
                millisLeft.setValue(remaining);
            }

            @Override
            public void onFinish() {
                timerState.setValue(TimeoutState.EXPIRED);
            }
        };
    }

    public void initializeTimeout(long totalDuration) {
        storage.getEditor()
                .clear()
                .putLong(TimeoutStorage.TOTAL_DURATION, totalDuration)
                .putLong(TimeoutStorage.EXPIRED_TIME, System.currentTimeMillis() + totalDuration)
                .putLong(TimeoutStorage.REMAINING_TIME, totalDuration)
                .putBoolean(TimeoutStorage.IS_RUNNING, true)
                .apply();
    }

    public void clearTimeout() {
        storage.getEditor().clear().apply();
        totalDuration = 0;
    }

    public boolean hasSavedTimer() {
        return storage.hasTimer();
    }

    // Will call this one at fragment `onResume`
    public void saveTimerToStorage() {
        long remaining = (millisLeft.getValue() == null) ? 0 : millisLeft.getValue();
        long expiredTime = System.currentTimeMillis() + remaining;
        storage.getEditor()
                .putBoolean(
                        TimeoutStorage.IS_RUNNING,
                        timerState.getValue() == TimeoutState.RUNNING)
                .putLong(TimeoutStorage.TOTAL_DURATION, totalDuration)
                .putLong(TimeoutStorage.REMAINING_TIME, remaining)
                .putLong(TimeoutStorage.EXPIRED_TIME, expiredTime)
                .apply();
    }
}
