package org.linphone.assistant;
/*
LoginFragment.java
Copyright (C) 2017  Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import org.linphone.R;
import org.linphone.core.TransportType;

public class LoginFragment extends Fragment implements OnClickListener, TextWatcher {
    private EditText mLogin, mUserid, mPassword, mDomain, mDisplayName;
    private RadioGroup mTransports;
    private Button mApply, mQrCode;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.assistant_login, container, false);

        mLogin = view.findViewById(R.id.assistant_username);
        mLogin.addTextChangedListener(this);
        mPassword = view.findViewById(R.id.assistant_password);
        mPassword.addTextChangedListener(this);
        mDomain = view.findViewById(R.id.assistant_domain);
        mDomain.setText(R.string.neth_test_domain);
        mDomain.addTextChangedListener(this);
        mApply = view.findViewById(R.id.assistant_apply);
        mApply.setEnabled(false);
        mApply.setOnClickListener(this);
        mQrCode = view.findViewById(R.id.lauch_qrcode_mahahahah);
        mQrCode.setOnClickListener(this);

        if (getArguments() != null && !getArguments().getString("RemoteUrl").isEmpty()) {
            String toSplit = getArguments().getString("RemoteUrl");
            String[] separated = toSplit.split(";");
            mLogin.setText(separated[0]);
            mPassword.setText(separated[1]);
            mDomain.setText(separated[2]);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.assistant_apply) {
            if (mLogin.getText() == null
                    || mLogin.length() == 0
                    || mPassword.getText() == null
                    || mPassword.length() == 0
                    || mDomain.getText() == null
                    || mDomain.length() == 0) {
                Toast.makeText(
                                getActivity(),
                                getString(R.string.first_launch_no_login_password),
                                Toast.LENGTH_LONG)
                        .show();
                return;
            }

            if (mDomain.getText().toString().compareTo(getString(R.string.default_domain)) == 0) {
                AssistantActivity.instance()
                        .displayLoginLinphone(
                                mLogin.getText().toString(), mPassword.getText().toString());
            } else {
                /*
                 * We have forced the user to use TLS instead other protocols.
                 * We don't need the userid and displayname: removed.
                 */
                AssistantActivity.instance()
                        .genericLogIn(
                                mLogin.getText().toString(),
                                null,
                                mPassword.getText().toString(),
                                null,
                                null,
                                mDomain.getText().toString(),
                                TransportType.Tls);
            }
        }

        if (id == R.id.lauch_qrcode_mahahahah) {
            AssistantActivity.instance().displayQRCodeReader();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mApply.setEnabled(
                !mLogin.getText().toString().isEmpty()
                        && !mPassword.getText().toString().isEmpty()
                        && !mDomain.getText().toString().isEmpty());
    }

    @Override
    public void afterTextChanged(Editable s) {}
}
