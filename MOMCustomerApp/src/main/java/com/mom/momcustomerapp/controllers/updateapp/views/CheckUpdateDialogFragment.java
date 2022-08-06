package com.mom.momcustomerapp.controllers.updateapp.views;
/*
 * Created by nishant on 13/07/17.
 */

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mom.momcustomerapp.controllers.updateapp.VersionModel;
import com.mom.momcustomerapp.R;
import com.mom.momcustomerapp.controllers.updateapp.WhatsNewModel;

public class CheckUpdateDialogFragment extends DialogFragment {

    public static final String FRAGMENT_TAG = "CheckUpdateDialogFragment";

    private VersionModel versionModel;
    private boolean isForced = false;

    private String whatsNew = "";

    private TextView versionNameTextView, whatsNewTextView;

    private Button laterButton, okButton;

    private FileDownloadListener mFileDownloadListener;

    public CheckUpdateDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_update_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        versionNameTextView = view.findViewById(R.id.update_dialog_tv_versionname);
        whatsNewTextView = view.findViewById(R.id.update_dialog_tv_whats_new);

        laterButton = (Button) view.findViewById(R.id.update_dialog_button_later);
        okButton = (Button) view.findViewById(R.id.update_dialog_button_ok);

        Bundle bundle = getArguments();
        this.versionModel = bundle.getParcelable("versionModel");
        this.isForced = bundle.getBoolean("forced");

        versionNameTextView.setText("Version : " + versionModel.getCurrentVersionName());

        whatsNew = "\n";
        for (int i = 0; i < versionModel.getWhatsnew().size(); i++) {
            WhatsNewModel whatsNewModel = versionModel.getWhatsnew().get(i);
            whatsNew = whatsNew + whatsNewModel.getCode() + " (" + whatsNewModel.getName() + ")\n";
            whatsNew = whatsNew + whatsNewModel.getChangelog() + "\n\n";
        }

        whatsNewTextView.setText(whatsNew);

        if (isForced) {
            laterButton.setText("Close App");
        }

        laterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if (isForced) {
                    getActivity().finish();
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMarket();
            }
        });

        setCancelable(false);
        return view;
    }

    public FileDownloadListener getFileDownloadListener() {
        return mFileDownloadListener;
    }

    public void setFileDownloadListener(FileDownloadListener fileDownloadListener) {
        this.mFileDownloadListener = fileDownloadListener;
    }

    private void goToMarket() {
//        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(versionModel.getUrl())));

        String filename = versionModel.getUrl();
        /*filename = filename.substring(filename.indexOf("Mventry"));
        filename = filename.replaceAll("\\+", " ");*/
        DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(versionModel.getUrl()));
        downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(downloadRequest);
        if (mFileDownloadListener != null) {
            mFileDownloadListener.onFileDownloadClicked();
        }
        this.dismiss();
    }

    public interface FileDownloadListener {
        void onFileDownloadClicked();
    }

}
