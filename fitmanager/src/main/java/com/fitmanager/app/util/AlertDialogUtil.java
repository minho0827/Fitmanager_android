package com.fitmanager.app.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import com.fitmanager.app.R;

import org.apache.commons.lang3.StringUtils;



public class AlertDialogUtil {

	/**
	 * AlertDialog
     ************************************************************************************************************************************************/
	private static AlertDialog dialog;

	/**
	 * 기본 팝업		:: 메세지 1개 & 버튼 1개
     ************************************************************************************************************************************************/
	public static void showSingleDialog(Context context, String message, OnSingleClickListener positiveCallback) {

		dismissDialog();

		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.layout_alert_single_popup, null);

		AppCompatTextView txtMessage = v.findViewById(R.id.txtMessage);
		AppCompatTextView btnConfirm = v.findViewById(R.id.btnConfirm);

		if(StringUtils.isNotEmpty(message)) {
			txtMessage.setText(message);
		}

		// 확인버튼
		if(positiveCallback == null) {
			positiveCallback = new OnSingleClickListener() {
				@Override
				public void onSingleClick(View v) {
					dismissDialog();
				}
			};
		}

		btnConfirm.setOnClickListener(positiveCallback);
		btnConfirm.setVisibility(View.VISIBLE);

		builder.setView(v);

		dialog = builder.create();
		dialog.setCancelable(false);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.show();
	}

	/**
	 * 기본 팝업 		:: 메세지 1개 / 버튼 2개
	 ************************************************************************************************************************************************/
	public static void showDoubleDialog(Context context, String message, OnSingleClickListener positiveCallback, OnSingleClickListener negativeCallback) {

		dismissDialog();

		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.layout_alert_double_popup, null);

		AppCompatTextView txtMessage = v.findViewById(R.id.txtMessage);
		AppCompatTextView btnCancel = v.findViewById(R.id.btnCancel);
		AppCompatTextView btnOk = v.findViewById(R.id.btnOk);

		if(StringUtils.isNotEmpty(message)) {
			txtMessage.setText(message);
		}

		// 예
		if(positiveCallback == null) {
			positiveCallback = new OnSingleClickListener() {
				@Override
				public void onSingleClick(View v) {
					dismissDialog();
				}
			};
		}

		// 아니오
		if(negativeCallback == null) {
			negativeCallback = new OnSingleClickListener() {
				@Override
				public void onSingleClick(View v) {
					dismissDialog();
				}
			};
		}

		btnCancel.setOnClickListener(negativeCallback);
		btnOk.setOnClickListener(positiveCallback);

		btnCancel.setVisibility(View.VISIBLE);
		btnOk.setVisibility(View.VISIBLE);

		builder.setView(v);

		dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * 기본 팝업		:: 메세지 2개 & 버튼 1개
	 ************************************************************************************************************************************************/
	public static void showSingleLargeDialog(Context context, String message, String address, OnSingleClickListener positiveCallback) {

		dismissDialog();

		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.layout_alert_single_large_popup, null);

		AppCompatTextView txtMessage = v.findViewById(R.id.txtMessage);
		AppCompatTextView txtMessage_address = v.findViewById(R.id.txtMessage_address);
		AppCompatTextView btnOk = v.findViewById(R.id.btnOk);

		if(StringUtils.isNotEmpty(message)) {
			txtMessage.setText(message);
		}

		if(StringUtils.isNotEmpty(address)) {
			txtMessage_address.setText(address);
		}

		// 예
		if(positiveCallback == null) {
			positiveCallback = new OnSingleClickListener() {
				@Override
				public void onSingleClick(View v) {
					dismissDialog();
				}
			};
		}

		btnOk.setOnClickListener(positiveCallback);

		btnOk.setVisibility(View.VISIBLE);

		builder.setView(v);

		dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * 기본 팝업	    :: 메세지 2개 & 버튼 2개
	 ************************************************************************************************************************************************/
	public static void showDoubleLargeDialog(Context context, String message, String address, OnSingleClickListener positiveCallback, OnSingleClickListener negativeCallback) {

		dismissDialog();

		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.layout_alert_double_large_popup, null);

		AppCompatTextView txtMessage = v.findViewById(R.id.txtMessage);
		AppCompatTextView txtMessage_address = v.findViewById(R.id.txtMessage_address);
		AppCompatTextView btnCancel = v.findViewById(R.id.btnCancel);
		AppCompatTextView btnOk = v.findViewById(R.id.btnOk);

		if(StringUtils.isNotEmpty(message)) {
			txtMessage.setText(message);
		}

		if(StringUtils.isNotEmpty(address)) {
			txtMessage_address.setText(address);
		}

		// 예
		if(positiveCallback == null) {
			positiveCallback = new OnSingleClickListener() {
				@Override
				public void onSingleClick(View v) {
					dismissDialog();
				}
			};
		}

		// 아니오
		if(negativeCallback == null) {
			negativeCallback = new OnSingleClickListener() {
				@Override
				public void onSingleClick(View v) {
					dismissDialog();
				}
			};
		}

		btnCancel.setOnClickListener(negativeCallback);
		btnOk.setOnClickListener(positiveCallback);

		btnCancel.setVisibility(View.VISIBLE);
		btnOk.setVisibility(View.VISIBLE);

		builder.setView(v);

		dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * 세션 팝업		:: 이미지 1개 & 메세지 1개 & 버튼 1개
	 ************************************************************************************************************************************************/
	public static void showSingleSessionDialog(Context context, String message, OnSingleClickListener positiveCallback) {

		dismissDialog();

		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.layout_alert_session_popup, null);

		AppCompatTextView txtMessage = v.findViewById(R.id.txtMessage);
		AppCompatTextView btnConfirm = v.findViewById(R.id.btnConfirm);

		if(StringUtils.isNotEmpty(message)) {
			txtMessage.setText(message);
		}

		// 확인버튼
		if(positiveCallback == null) {
			positiveCallback = new OnSingleClickListener() {
				@Override
				public void onSingleClick(View v) {
					dismissDialog();
				}
			};
		}
		btnConfirm.setOnClickListener(positiveCallback);
		btnConfirm.setVisibility(View.VISIBLE);

		builder.setView(v);

		dialog = builder.create();
		dialog.setCancelable(false);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.show();
	}

    /**
     * Splash 팝업	:: 메세지 2개 & 버튼 1개
     ************************************************************************************************************************************************/
    public static void showNoticeDialog(Context context, String message, String address, OnSingleClickListener positiveCallback, DialogInterface.OnCancelListener onCancelListener) {

        dismissDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.layout_alert_notice_popup, null);

        AppCompatTextView txtMessage = v.findViewById(R.id.txtMessage);
        AppCompatTextView txtMessage_address = v.findViewById(R.id.txtMessage_address);
        AppCompatTextView btnOk = v.findViewById(R.id.btnOk);

        if(StringUtils.isNotEmpty(message)) {
            txtMessage.setText(message);
        }

        if(StringUtils.isNotEmpty(address)) {
            txtMessage_address.setText(address);
        }

        // 확인 버튼
        if(positiveCallback == null) {
            positiveCallback = new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    dismissDialog();
                }
            };
        }

        // 다이얼로그 외부 터치 시
        if(onCancelListener == null) {
            onCancelListener = dialogInterface -> dismissDialog();
        }

        btnOk.setOnClickListener(positiveCallback);

        btnOk.setVisibility(View.VISIBLE);

        builder.setView(v);

        dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setOnCancelListener(onCancelListener);
        dialog.show();
    }

	/**
	 *  메세지 2개 & 버튼 2개
	 ************************************************************************************************************************************************/
	public static void showDoubleWalletCopyDialog(Context context, String message, String address, OnSingleClickListener positiveCallback, OnSingleClickListener negativeCallback) {

		dismissDialog();

		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.layout_alert_double_large_popup, null);

		AppCompatTextView txtMessage = v.findViewById(R.id.txtMessage);
		AppCompatTextView txtMessage_address = v.findViewById(R.id.txtMessage_address);
		AppCompatTextView btnCancel = v.findViewById(R.id.btnCancel);
		AppCompatTextView btnOk = v.findViewById(R.id.btnOk);

		btnCancel.setText(context.getString(R.string.str_cancel));

		if(StringUtils.isNotEmpty(message)) {
			txtMessage.setText(message);
		}

		if(StringUtils.isNotEmpty(address)) {
			txtMessage_address.setText(address);
		}

		// 예
		if(positiveCallback == null) {
			positiveCallback = new OnSingleClickListener() {
				@Override
				public void onSingleClick(View v) {
					dismissDialog();
				}
			};
		}

		// 아니오
		if(negativeCallback == null) {
			negativeCallback = new OnSingleClickListener() {
				@Override
				public void onSingleClick(View v) {
					dismissDialog();
				}
			};
		}

		btnCancel.setOnClickListener(negativeCallback);
		btnOk.setOnClickListener(positiveCallback);

		btnCancel.setVisibility(View.VISIBLE);
		btnOk.setVisibility(View.VISIBLE);

		builder.setView(v);

		dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * 권한 팝업	    :: 메세지 1개 / 버튼 2개
     ************************************************************************************************************************************************/
	public static void showPermissionCheckDialog(Context context, String message, OnSingleClickListener positiveCallback, OnSingleClickListener negativeCallback) {

		dismissDialog();

		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.layout_alert_permission_popup, null);

		AppCompatTextView txtMessage = v.findViewById(R.id.txtMessage);
        AppCompatTextView btnCancel = v.findViewById(R.id.btnCancel);
        AppCompatTextView btnOk = v.findViewById(R.id.btnOk);

		if(StringUtils.isNotEmpty(message)) {
			txtMessage.setText(message);
		}

		// 예
        if(positiveCallback == null) {
            positiveCallback = new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    dismissDialog();
                }
            };
        }

        // 아니오
        if(negativeCallback == null) {
			negativeCallback = new OnSingleClickListener() {
				@Override
				public void onSingleClick(View v) {
					dismissDialog();
				}
			};
		}

		btnCancel.setOnClickListener(negativeCallback);
		btnOk.setOnClickListener(positiveCallback);

		btnCancel.setVisibility(View.VISIBLE);
		btnOk.setVisibility(View.VISIBLE);

		builder.setView(v);

		dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * 메인 팝업		:: 고정 메세지 & 버튼 2개 & 체크박스 1개
	 ************************************************************************************************************************************************/
	public static void showMainNoticeDialog(Context context, OnSingleClickListener okCallback, OnSingleClickListener otpCallback, CompoundButton.OnCheckedChangeListener checkbox) {

		dismissDialog();

		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.layout_alert_main_notice_popup, null);

		AppCompatTextView txt_sms_auth = v.findViewById(R.id.txt_sms_auth);
		AppCompatTextView txt_otp_auth = v.findViewById(R.id.txt_otp_auth);
		AppCompatTextView btn_ok = v.findViewById(R.id.btn_ok);
		AppCompatTextView btn_otp = v.findViewById(R.id.btn_otp);
        AppCompatCheckBox cb_not_review = v.findViewById(R.id.cb_not_review);

        SpannableStringBuilder stringBuilderSMS = new SpannableStringBuilder(txt_sms_auth.getText().toString());
        SpannableStringBuilder stringBuilderOTP = new SpannableStringBuilder(txt_otp_auth.getText().toString());

        stringBuilderSMS.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, txt_sms_auth.getText().toString().indexOf(":"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilderOTP.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, txt_otp_auth.getText().toString().indexOf(":"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		// OK
		if(okCallback == null) {
            okCallback = new OnSingleClickListener() {
				@Override
				public void onSingleClick(View v) {
					dismissDialog();
				}
			};
		}

		// OTP
		if(otpCallback == null) {
            otpCallback = new OnSingleClickListener() {
				@Override
				public void onSingleClick(View v) {
					dismissDialog();
				}
			};
		}

		txt_sms_auth.setText(stringBuilderSMS);
        txt_otp_auth.setText(stringBuilderOTP);
        btn_ok.setOnClickListener(okCallback);
        btn_otp.setOnClickListener(otpCallback);
		cb_not_review.setOnCheckedChangeListener(checkbox);

		builder.setView(v);

		dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * Dialog 종료
     ************************************************************************************************************************************************/
	public static void dismissDialog() {
		if (dialog != null && dialog.isShowing()) {
			try {
				dialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dialog = null;
			}
		}
	}
}