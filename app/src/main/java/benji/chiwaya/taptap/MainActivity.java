package benji.chiwaya.taptap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = "TapTap App";
    private AtomicBoolean START = new AtomicBoolean(false);
    private int CHANCE = 1;
    private int SCORE = 0;
    private int SPEED = 2000;
    private String[] colors_array;
    private int[] backgrounds_array;
    private int Level_up = 50;
    private TextView colors_text_view, high_score, new_score, game_over;
    private Button gameButton;
    private Dialog dialog;
    private Thread t;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colors_text_view = findViewById(R.id.text);
        
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
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            
                            Thread.sleep(getSPEED());
                            runOnUiThread(() -> {
                                rotate_colors(colors_text_view);
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
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
        t.setTextColor(backgrounds_array[background]);
    }
    
    protected boolean Verify(TextView t) {
        int color =t.getCurrentTextColor();
        
        switch (color)
        {
            case Color.RED:    if(t.getText().toString() == colors_array[0])
            {
                Toast.makeText(getApplicationContext(),"POINT",Toast.LENGTH_SHORT).show();
                return true;
            }
            else{
                return false;}
            
            case Color.GREEN:  if(t.getText().toString() == colors_array[3])
            {
                Toast.makeText(getApplicationContext(),"POINT",Toast.LENGTH_SHORT).show();
                return true;
            }
            else{
                return false;}
            
            case Color.BLUE:   if(t.getText().toString() == colors_array[2])
            {
                return true;
            }
            else{
                return false;}
            
            case Color.YELLOW: if(t.getText().toString() == colors_array[1])
            {
                Toast.makeText(getApplicationContext(),"POINT",Toast.LENGTH_SHORT).show();
                return  true;
            }
            else{
                return false;}
            
            case Color.MAGENTA:if(t.getText().toString() == colors_array[4])
            {
                Toast.makeText(getApplicationContext(),"POINT", Toast.LENGTH_SHORT).show();
                return true;
            }
            else{
                return false;}
            
            default:
                return false;
        }
    }
    
    public void tvTap(View view) {
        
        if(Verify(colors_text_view)){
            
            SCORE+=10;
            Log.e(TAG,String.valueOf(SCORE));
            rotate_colors(colors_text_view);
            
            if(SCORE == Level_up)
            {
                setSPEED(SPEED-=500);
                Level_up+=30;
            }
        }
        else {
            CHANCE--;
            }
        if(CHANCE == 0)
        {
            dialog.show();
            high_score.setText(getString(R.string.high_score)+String.valueOf(SCORE*10));
            new_score.setText(getString(R.string.new_score)+String.valueOf(SCORE));
            game_over.setVisibility(VISIBLE);
        }
    }
    
    private void reset()
    {
        START.set(true);
        CHANCE = 4;
        SCORE = 10;
        SPEED = 3000;
        dialog.dismiss();
    }
    
    
    public int getSPEED() {
        return SPEED;
    }
    
    public void setSPEED(int SPEED) {
        this.SPEED = SPEED;
    }
}
