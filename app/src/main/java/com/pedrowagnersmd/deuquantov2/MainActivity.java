package com.pedrowagnersmd.deuquantov2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

    private TextInputEditText editTextValor, editTextQtd;
    private TextView textResultado;
    private FloatingActionButton btnShare, btnSound;
    TextToSpeech ttsPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextValor = findViewById(R.id.editTextValor);
        editTextQtd = findViewById(R.id.editTextQtd);
        textResultado = findViewById(R.id.textResultado);

        editTextValor.addTextChangedListener(this);
        editTextQtd.addTextChangedListener(this);

        btnShare = findViewById(R.id.btnShare);
        btnSound = findViewById(R.id.btnSound);

        btnShare.setOnClickListener(this);
        btnSound.setOnClickListener(this);

        Intent checkTTS = new Intent();
        checkTTS.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTS, 1122);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1122){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                ttsPlayer = new TextToSpeech(this, this);
            }else {
                Intent installTTS = new Intent();
                installTTS.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTS);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String valorRecuperado = editTextValor.getText().toString();
        String qtdPessoasRecuperado = editTextQtd.getText().toString();
        DecimalFormat df = new DecimalFormat("#.00");
        if(valorRecuperado == null || valorRecuperado.equals("") || qtdPessoasRecuperado == null || qtdPessoasRecuperado.equals("")){
            Toast.makeText(getApplicationContext(), "Preencha todos os campos!", Toast.LENGTH_LONG).show();
        }else{
            double valorConta =Double.parseDouble(valorRecuperado);
            int qtdPessoas = Integer.parseInt(qtdPessoasRecuperado);
            double res = valorConta/qtdPessoas;
            String resultado = df.format(res);
            textResultado.setText("Deu R$ " + resultado + " pra cada.");
        }
    }

    @Override
    public void onClick(View view) {
        if(view == btnShare) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, textResultado.getText().toString());
            startActivity(intent);
        }
        if(view == btnSound) {
            if(ttsPlayer != null) {
                ttsPlayer.speak(textResultado.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "ID1");
            }
        }
    }

    @Override
    public void onInit(int initStatus) {
        Locale locale = new Locale("PT", "BR");

        if(initStatus == TextToSpeech.SUCCESS){
            ttsPlayer.setLanguage(locale);
            Toast.makeText(this, "TTS Ativado", Toast.LENGTH_LONG).show();
        }else if(initStatus == TextToSpeech.ERROR){
            Toast.makeText(this, "Sem TTS ativado", Toast.LENGTH_LONG).show();
        }
    }
}