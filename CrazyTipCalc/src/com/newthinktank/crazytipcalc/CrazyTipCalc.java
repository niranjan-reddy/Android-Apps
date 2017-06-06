package com.newthinktank.crazytipcalc;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class CrazyTipCalc extends Activity {
	
	// Constants used when saving and restoring
	
	private static final String TOTAL_BILL = "TOTAL_BILL";
	private static final String CURRENT_TIP = "CURRENT_TIP";
	private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";
	
	private double billBeforeTip; // Users bill before tip
	private double tipAmount; // Tip amount
	private double finalBill; // Bill plus Tip
	
	EditText billBeforeTipET;
	EditText tipAmountET;
	EditText finalBillET;
	
	// NEW PART ---------------
	
	// Sum of all radio buttons and check boxes
	
	private int[] checklistValues = new int[12]; 
	
	// Declare CheckBoxes
	
	CheckBox friendlyCheckBox;
	CheckBox specialsCheckBox;
	CheckBox opinionCheckBox;
	
	// Declare RadioButtons
	
	RadioGroup availableRadioGroup;
	RadioButton availableBadRadio;
	RadioButton availableOKRadio;
	RadioButton availableGoodRadio;
	
	// Declare Spinner (Drop Down Box)
	
	Spinner problemsSpinner;
	
	// Declare Buttons
	
	Button startChronometerButton;
	Button pauseChronometerButton;
	Button resetChronometerButton;
	
	// Declare Chronometer
	
	Chronometer timeWaitingChronometer;
	
	// The number of seconds you spent 
	// waiting for the waitress
	
	long secondsYouWaited = 0;
	
	// TextView for the chronometer
	
	TextView timeWaitingTextView;
	
	
	// END OF NEW PART ---------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crazy_tip_calc); // Inflate the GUI
		
		// Check if app just started, or if it is being restored
		
		if(savedInstanceState == null){
			
			// Just started
			
			billBeforeTip = 0.0;
			tipAmount = .15; 
			finalBill = 0.0; 
			
		} else {
			
			// App is being restored
			
			billBeforeTip = savedInstanceState.getDouble(BILL_WITHOUT_TIP);
			tipAmount = savedInstanceState.getDouble(CURRENT_TIP); 
			finalBill = savedInstanceState.getDouble(TOTAL_BILL); 
			
		}
		
		// Initialize the EditTexts
		
		billBeforeTipET = (EditText) findViewById(R.id.billEditText); // Users bill before tip
		tipAmountET = (EditText) findViewById(R.id.tipEditText); // Tip amount
		finalBillET = (EditText) findViewById(R.id.finalBillEditText); // Bill plus tip
		
		// Initialize the SeekBar and add a ChangeListener
		
		tipSeekBar = (SeekBar) findViewById(R.id.changeTipSeekBar);
		
		tipSeekBar.setOnSeekBarChangeListener(tipSeekBarListener);
		
		// ---------------------------
		
		// Add change listener for when the bill before tip is changed
		
		billBeforeTipET.addTextChangedListener(billBeforeTipListener);
		
		// NEW PART ---------------
		
		// Initialize CheckBoxs
		
		friendlyCheckBox = (CheckBox) findViewById(R.id.friendlyCheckBox);
		specialsCheckBox = (CheckBox) findViewById(R.id.specialsCheckBox);
		opinionCheckBox = (CheckBox) findViewById(R.id.opinionCheckBox);
		
		setUpIntroCheckBoxes(); // Add change listeners to check boxes
		
		// Initialize RadioButtons
		
		availableBadRadio = (RadioButton) findViewById(R.id.availableBadRadio);
		availableOKRadio = (RadioButton) findViewById(R.id.availableOKRadio);
		availableGoodRadio = (RadioButton) findViewById(R.id.availableGoodRadio);
		
		// Initialize RadioGroups
		
		availableRadioGroup = (RadioGroup) findViewById(R.id.availableRadioGroup);
		
		// Add ChangeListener To Radio buttons
		
		addChangeListenerToRadios();
		
		// Initialize the Spinner
		
		problemsSpinner = (Spinner) findViewById(R.id.problemsSpinner);
		
		problemsSpinner.setPrompt("Problem Solving");
		
		// Add ItemSelectedListener To Spinner
		
		addItemSelectedListenerToSpinner();
		
		// Initialize Buttons
		
		startChronometerButton = (Button) findViewById(R.id.startChronometerButton);
		pauseChronometerButton = (Button) findViewById(R.id.pauseChronometerButton);
		resetChronometerButton = (Button) findViewById(R.id.resetChronometerButton);
		
		// Add setOnClickListeners for buttons
		
		setButtonOnClickListeners();
		
		// Initialize Chronometer
		
		timeWaitingChronometer = (Chronometer) findViewById(R.id.timeWaitingChronometer);
		
		// TextView for Chronometer
		
		timeWaitingTextView = (TextView) findViewById(R.id.timeWaitingTextView);
		
		// END OF NEW PART ---------------
	}
	
	// Called when the bill before tip amount is changed
	
	private TextWatcher billBeforeTipListener = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			
			try{
				
				// Change the billBeforeTip to the new input
				
				billBeforeTip = Double.parseDouble(arg0.toString());
				
			}
			
			catch(NumberFormatException e){
				
				billBeforeTip = 0.0;
				
			}
			
			updateTipAndFinalBill();
			
		}
		
	};
	
	// Update the tip amount and add tip to bill to
	// find the final bill amount
	
	private void updateTipAndFinalBill(){
		
		// Get tip amount
		
		double tipAmount = Double.parseDouble(tipAmountET.getText().toString());
		
		// The bill before tip amount was set in billBeforeTipListener
		
		// Get the bill plus the tip
		
		double finalBill = billBeforeTip + (billBeforeTip * tipAmount);
		
		// Set the total bill amount including the tip
		// Convert into a 2 decimal place String
		
		finalBillET.setText(String.format("%.02f", finalBill));
		
	}
	
	// Called when a device changes in some way. For example,
	// when a keyboard is popped out, or when the device is 
	// rotated. Used to save state information that you'd like
	// to be made available.
	
	@Override
	protected void onSaveInstanceState(Bundle outState){
		
		super.onSaveInstanceState(outState);
		
		outState.putDouble(TOTAL_BILL, finalBill);
		outState.putDouble(CURRENT_TIP, tipAmount);
		outState.putDouble(BILL_WITHOUT_TIP, billBeforeTip);
		
	}
	
	// SeekBar used to make a custom tip
	
	private SeekBar tipSeekBar;
	
	private OnSeekBarChangeListener tipSeekBarListener = new OnSeekBarChangeListener(){

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			
			// Get the value set on the SeekBar
			
			tipAmount = (tipSeekBar.getProgress()) * .01;
			
			// Set tipAmountET with the value from the SeekBar
			
			tipAmountET.setText(String.format("%.02f", tipAmount));
			
			// Update all the other EditTexts
			
			updateTipAndFinalBill();
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};

@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crazy_tip_calc, menu);
		return true;
	}

}