package db.dialservices;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Diogo Bernardino
 */

public class MainActivity extends Activity {

	/* Fields */
	private static final int CONTACT_PICKER_RESULT = 1001;
	private String usingCode;

	/* Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TypedArray list = getResources().obtainTypedArray(R.array.ussdcodes);

		LayoutInflater inflater = this.getLayoutInflater();
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);

		/* Initialize each JSON object of "ussd_info.xml". */
		for (int i = 0; i < list.length(); i++) {
			JSONObject j = null;
			try {
				j = new JSONObject(list.getString(i));
				View v = inflater.inflate(R.layout.list_item, null);

				LinearLayout l = (LinearLayout) v.findViewById(R.id.linear);

				final String code = j.getString("code");
				Button bar = (Button) l.findViewById(R.id.bar);
				bar.setText(j.getString("title"));
				bar.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (code.contains("contact")) {
							usingCode = code;
							startActivityForResult(new Intent(
									Intent.ACTION_PICK, Contacts.CONTENT_URI),
									CONTACT_PICKER_RESULT);
						} else {
							startActivity(new Intent(
									"android.intent.action.CALL", Uri
											.parse("tel:"
													+ code.replaceAll("#",
															Uri.encode("#")))));
						}
					}
				});

				final String description = j.getString("description");
				((Button) l.findViewById(R.id.ussdinfobutton))
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								final AlertDialog.Builder builder = new AlertDialog.Builder(
										MainActivity.this);
								View view = LayoutInflater.from(
										MainActivity.this).inflate(
										R.layout.info_layout, null);
								((TextView) view.findViewById(R.id.ussdcode))
										.setText(code);
								((TextView) view
										.findViewById(R.id.ussddescription))
										.setText(description);
								builder.setView(view);
								builder.setCancelable(true).setNeutralButton(
										"Ok",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													final DialogInterface dialog,
													@SuppressWarnings("unused") final int id) {
												dialog.cancel();
											}
										});
								builder.create().show();
							}
						});

				ll.addView(v);
			} catch (JSONException e) {
				Log.e("log", "XML information failed");
			}
		}
		setContentView(ll);
	}

	/* Threat the number: "-" and prefixes */
	String threatPhoneNumber(String phoneNumber) {
		phoneNumber = phoneNumber.replaceAll("-", "");
		String prefix = getResources().getString(R.string.prefix);
		if (phoneNumber.startsWith(prefix))
			phoneNumber = phoneNumber.substring(prefix.length());
		else if (phoneNumber.startsWith("00" + prefix.substring(1)))
			phoneNumber = phoneNumber.substring(prefix.length() + 1);
		return phoneNumber;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CONTACT_PICKER_RESULT:
				Uri result = data.getData();
				String id = result.getLastPathSegment();
				Cursor pCur = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = ?", new String[] { id }, null);
				if (pCur.moveToNext()) {
					/* Edit phone number and call activity */
					String phoneNumber = pCur
							.getString(pCur
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					usingCode = usingCode.replaceAll("contact",
							threatPhoneNumber(phoneNumber));

					/*
					 * Encoding necessary characters. If your network has more
					 * characters than '#', do the same to them.
					 */
					usingCode = usingCode.replaceAll("#", Uri.encode("#"));

					startActivity(new Intent("android.intent.action.CALL",
							Uri.parse("tel:" + usingCode)));
				}
				pCur.close();
				break;
			}
		}
	}

	/* Menu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.generalmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.iwebsite:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://github.com/diogobernardino"));
			startActivity(browserIntent);
			break;
		}
		return true;
	}

}
