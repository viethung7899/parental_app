package ca.sfu.fluorine.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;

import ca.sfu.fluorine.parentapp.databinding.ActivityHelpBinding;

public class HelpActivity extends AppCompatActivity {
    private ActivityHelpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.appIconCitation.setMovementMethod(LinkMovementMethod.getInstance());
        binding.childIconCitation.setMovementMethod(LinkMovementMethod.getInstance());
        binding.coinIconCitation.setMovementMethod(LinkMovementMethod.getInstance());
        binding.pulsatorCitation.setMovementMethod(LinkMovementMethod.getInstance());
        binding.taskIconCitation.setMovementMethod(LinkMovementMethod.getInstance());
        binding.timerIconCitation.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
