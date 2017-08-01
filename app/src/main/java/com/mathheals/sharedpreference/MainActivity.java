package com.mathheals.sharedpreference;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class MainActivity extends Activity {
    Button kayit,giris;
    EditText mail,sifre;
    String mail_string,sifre_string;
    SharedPreferences preferences;//preferences referansı
    SharedPreferences.Editor editor; //preferences editor nesnesi referansı .prefernces nesnesine veri ekleyip cıkarmak için
    Boolean hatirla;
    CheckBox chkHatirla;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences objesi
        editor = preferences.edit(); //aynı şekil editor nesnesi oluşturuluyor


        //Burda sharedPreferencas üzerine kayıtlı login değerini alıyoruz.
        //Login değeri doğru giriş yapıldığında veya kayıt olduğunda true olarak kaydedilir
        //Amacı ise kullanıcı uygulamadan cıkarken direk çıkıs demeden cıktıysa yanı direk home veya back tusuyla uygulamadan çıktıysa
        //Geri geldiğinde tekrar giriş bilgilerini istemeden anasayfaya yönlendiriyoruz
        //Bu değer ancak anasayfa üzerinde cıkış butonuna basılırsa diğer bilgiler silinmeden bu değer false yapılır
        //ve uygulamaya tekrar girildiğinde kayıt olurken kullandığı şifre ve emaili ister
        if(preferences.getBoolean("login", false)){
            Intent i = new Intent(getApplicationContext(),Anasayfa.class);
            startActivity(i);
            finish();
        }


        mail = (EditText)findViewById(R.id.editText1);
        sifre = (EditText)findViewById(R.id.editText2);


//hatırla checkbox ı için
        hatirla=preferences.getBoolean("hatirla",false);
        chkHatirla=(CheckBox)findViewById(R.id.checkBox);
        if(hatirla==true){

            mail.setText(preferences.getString("email",""));
            sifre.setText(preferences.getString("sifre",""));
            chkHatirla.setChecked(true);
        }



        kayit = (Button) findViewById(R.id.button1);
        kayit.setOnClickListener(new View.OnClickListener() {//kayıt ekranı açılır

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Kayit.class);
                startActivity(i);
            }
        });

        giris = (Button) findViewById(R.id.button2);
        giris.setOnClickListener(new View.OnClickListener() {//giriş butonu tıklandığı zaman



            @Override
            public void onClick(View v) {
                mail_string = mail.getText().toString();
                sifre_string = sifre.getText().toString();


                //hatırla checkboxı için
                if (chkHatirla.isChecked())	{
                    editor.putBoolean("hatirla", true);
                    editor.putString("email", mail_string);
                    editor.putString("sifre", sifre_string);
                    editor.commit();
                } else {
                    editor.clear();
                    editor.commit();
                }


                if(mail_string.matches("") || sifre_string.matches("")){//bilgiler eksik   ise

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("Uyar˝");
                    alertDialog.setMessage("Eksiksiz Doldurunz!");
                    alertDialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

                        }
                    });
                    alertDialog.show();
                }else{//Bilgiler doğru ise

                    String email = preferences.getString("email", "");//preferences objesinden kaydettiğimiz değerleri key leri ile geri alıyoruz
                    String sifre = preferences.getString("sifre", "");//preferences objesinden kaydettiğimiz değerleri key leri ile geri alıyoruz

                    if(email.matches(mail_string) && sifre.matches(sifre_string)){//edittextlerden alınan şifre ve mail değerleri shared preferencesdan alınan değerlerle eşleşiyorsa

                        //Giriş başarılı ise sharedpreferences login değerini true yapıyoruz.
                        editor.putBoolean("login", true);
                        Intent i = new Intent(getApplicationContext(),Anasayfa.class);//Anasayfa aktivity si açılır
                        startActivity(i);
                        finish();
                    }else{//şifre ve mail uyuşmuyorsa hata bastırılır
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("Hata");
                        alertDialog.setMessage("Mailiniz veya Şifreniz Uyuşmuyor!");
                        alertDialog.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                            }
                        });
                        alertDialog.show();
                    }
                }
            }
        });
    }

}
