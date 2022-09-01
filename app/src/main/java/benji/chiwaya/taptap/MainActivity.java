package benji.chiwaya.taptap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.view.View.VISIBLE;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = "TapTap App";
    private AtomicBoolean START = new AtomicBoolean(false);
    private int CHANCE = 1;
    private int SCORE = 0;
    private int HIGHSCORE = 10;
    private int SPEED = 3000;
    private String[] colors_array;
    private int[] backgrounds_array;
    private int Level_up = 50;
    private SharedPreferences sp_database;
    private TextView colors_text_view, high_score, new_score, game_over;
    private Button gameButton;
    private Dialog dialog;
    private Thread t;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colors_text_view = findViewById(R.id.text);

        sp_database = getApplicationContext()
                .getSharedPreferences("Taptap", Context.MODE_PRIVATE);

        dialog = new Dialog(this);
        
        dialog.setContentView(R.layout.new_game);
        gameButton = dialog.findViewById(R.id.game_button);
        high_score = dialog.findViewById(R.id.highscore);
        new_score = dialog.findViewById(R.id.newscore);
        game_over = dialog.findViewById(R.id.gameOver);

        
        colors_array = getResources().getStringArray(R.array.colors_array);
        backgrounds_array = new int[]{Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.MAGENTA,};
        
        dialog.show();
        
        t = new Thread() {
                @Override
                public void run() {try {
                        while (!isInterrupted()) {
                            
                            Thread.sleep(getSPEED());
                            runOnUiThread(() -> {
                                rotate_colors(colors_text_view);
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }}
            };
        t.start();
        gameButton.setOnClickListener(view -> {
            reset();
        });

    }
    
    
    private void rotate_colors(TextView t) {
        
        Random random = new Random();
        Random r = new Random();
        // you have also handle min to max index
        
        int color = r.nextInt(colors_array.length);
        t.setText(colors_array[color]);
        
        int background = random.nextInt(backgrounds_array.length);
        findViewById(R.id.mainlayout).setBackgroundColor(backgrounds_array[background]);
    }
    
    protected boolean Verify(TextView t) {

        ColorDrawable viewColor = (ColorDrawable) (findViewById(R.id.mainlayout).getBackground());
        int color = viewColor.getColor();

        switch (color)
        {
            case Color.RED:     if(t.getText().toString() == colors_array[0])
                {
                return true;
                }
            case Color.GREEN:   if(t.getText().toString() == colors_array[3])
                {
                return true;
                }
            case Color.BLUE:    if(t.getText().toString() == colors_array[2])
                 {
                return true;
                 }
            case Color.YELLOW:  if(t.getText().toString() == colors_array[1])
                {
                return  true;
                }
            case Color.MAGENTA:if(t.getText().toString() == colors_array[4])
                {
                return true;
                }

            default:
                return false;
        }
    }
    
    public void tvTap(View view) {
        if(Verify(colors_text_view)){
            YoYo.with(Techniques.BounceIn)
                    .duration(100)
                    .playOn(colors_text_view);
            SCORE+=10;
            Log.e(TAG,String.valueOf(SCORE));
            rotate_colors(colors_text_view);
            
            if(SCORE == Level_up)
            {
                setSPEED(SPEED=SPEED-250);
                Level_up=Level_up+30;
            }
        }
        else if(CHANCE == 0){
            SharedPreferences.Editor save_highscore = sp_database.edit();

            if((sp_database.getInt("highscore",0) == 0 )|| (sp_database.getInt("highscore", 0)) <= SCORE)
            {
                save_highscore.putInt("highscore",SCORE);
                save_highscore.commit();
                HIGHSCORE = SCORE;
            }

            HIGHSCORE = sp_database.getInt("highscore",0);

            dialog.show();
            high_score.setText(getString(R.string.high_score)+(HIGHSCORE));
            new_score.setText(getString(R.string.new_score)+(SCORE));
            game_over.setVisibility(VISIBLE);
        }
        else {
            YoYo.with(Techniques.Tada)
                    .duration(100)
                    .playOn(colors_text_view);

            CHANCE--;
        }
    }
    
    private void reset()
    {
        SharedPreferences.Editor save_highscore = sp_database.edit();
        HIGHSCORE = sp_database.getInt("highscore",0);
        START.set(true);
        CHANCE = 4;
        SCORE = 10;
        SPEED = 3000;
        dialog.dismiss();

    }
    public int getSPEED()
    {
        return SPEED;
    }
    public void setSPEED(int SPEED) {
        this.SPEED = SPEED;
    }
}
