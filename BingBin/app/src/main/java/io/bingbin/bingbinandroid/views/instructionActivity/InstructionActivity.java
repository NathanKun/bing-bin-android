package io.bingbin.bingbinandroid.views.instructionActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.bingbin.bingbinandroid.R;
import io.bingbin.bingbinandroid.models.Category;

public class InstructionActivity extends AppCompatActivity {

    @BindView(R.id.instruction_title_textview)
    TextView instructionTitleTextview;
    @BindView(R.id.instruction_return_btn)
    Button instructionReturnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Category category = (Category) intent.getSerializableExtra("category");
        instructionTitleTextview.setText(category.getTrashbin().getFrenchName());
    }

    @OnClick(R.id.instruction_return_btn)
    void returnOnClick(View view) {
        finish();
    }

}
