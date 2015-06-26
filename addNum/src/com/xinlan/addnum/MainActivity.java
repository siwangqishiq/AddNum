package com.xinlan.addnum;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	private Button btn;
	private TextView resultText;
	private EditText num1Text, num2Text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btn = (Button) findViewById(R.id.btn);
		resultText = (TextView) findViewById(R.id.result_content);
		num1Text = (EditText) findViewById(R.id.num1);
		num2Text = (EditText) findViewById(R.id.num2);

		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String num1 = num1Text.getText().toString().trim();
		String num2 = num2Text.getText().toString().trim();
		if (TextUtils.isEmpty(num1) || TextUtils.isEmpty(num2))
			return;
		
		String resultStr = Native.addNum(num1, num2);
		resultText.setText(resultStr);
	}
}// end class
