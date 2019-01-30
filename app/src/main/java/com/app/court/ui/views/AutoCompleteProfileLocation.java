package com.app.court.ui.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.app.court.R;
import com.app.court.helpers.UIHelper;
import com.app.court.ui.adapters.AutoCompleteAdapter;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;


public class AutoCompleteProfileLocation extends android.support.v7.widget.AppCompatAutoCompleteTextView {

    private Drawable mCloseIcon;
    private GoogleApiClient mGoogleApiClient;
    private AutoCompleteAdapter mAutoCompleteAdapter;
    private com.app.court.ui.views.AutoCompleteLocation.AutoCompleteLocationListener mAutoCompleteLocationListener;

    public interface AutoCompleteLocation {
        void onTextClear();

        void onItemSelected(Place selectedPlace);
    }

    public AutoCompleteProfileLocation(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources resources = context.getResources();
        TypedArray typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.AutoCompleteLocation, 0, 0);
        Drawable background =
                typedArray.getDrawable(R.styleable.AutoCompleteLocation_background_layout);
        if (background == null) {
            background = resources.getDrawable(R.color.transparent);
        }
        String hintText = typedArray.getString(R.styleable.AutoCompleteLocation_hint_text);
        if (hintText == null) {
            hintText = resources.getString(R.string.address);
        }
        int hintTextColor = typedArray.getColor(R.styleable.AutoCompleteLocation_hint_text_color,
                resources.getColor(R.color.app_dark_gray));
        int textColor = typedArray.getColor(R.styleable.AutoCompleteLocation_text_color,
                resources.getColor(R.color.light_grey));
       int padding = resources.getDimensionPixelSize(R.dimen.x10);
        typedArray.recycle();

        setBackground(background);
        setHint(hintText);
        setHintTextColor(hintTextColor);
        setTextColor(textColor);
        //setPadding(padding, padding, padding, padding);
        setMaxLines(resources.getInteger(R.integer.default_max_lines));

        mCloseIcon = context.getResources().getDrawable(R.drawable.ic_close);
        mGoogleApiClient = new GoogleApiClient.Builder(context).addApi(Places.GEO_DATA_API)
                .addApi(AppIndex.API)
                .build();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mGoogleApiClient.connect();
        this.addTextChangedListener(mAutoCompleteTextWatcher);
        this.setOnTouchListener(mOnTouchListener);
        this.setOnItemClickListener(mAutocompleteClickListener);
        this.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter = new AutoCompleteAdapter(getContext(), mGoogleApiClient, null, null);
        this.setAdapter(mAutoCompleteAdapter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {
            this.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    com.app.court.ui.views.AutoCompleteProfileLocation.this.getText().toString().equals("") ? null : mCloseIcon, null);
        }
    }

    public void setAutoCompleteTextListener(
            com.app.court.ui.views.AutoCompleteLocation.AutoCompleteLocationListener autoCompleteLocationListener) {
        mAutoCompleteLocationListener = autoCompleteLocationListener;
    }

    private TextWatcher mAutoCompleteTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            com.app.court.ui.views.AutoCompleteProfileLocation.this.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    com.app.court.ui.views.AutoCompleteProfileLocation.this.getText().toString().equals("") ? null : mCloseIcon, null);
            if (mAutoCompleteLocationListener != null) {
                mAutoCompleteLocationListener.onTextClear();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getX()
                    > com.app.court.ui.views.AutoCompleteProfileLocation.this.getWidth()
                    - com.app.court.ui.views.AutoCompleteProfileLocation.this.getPaddingRight()
                    - mCloseIcon.getIntrinsicWidth()) {
                com.app.court.ui.views.AutoCompleteProfileLocation.this.setText("");
                com.app.court.ui.views.AutoCompleteProfileLocation.this.setCompoundDrawables(null, null, null, null);
            }
            return false;
        }
    };

    private AdapterView.OnItemClickListener mAutocompleteClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    UIHelper.hideSoftKeyboard(com.app.court.ui.views.AutoCompleteProfileLocation.this.getContext(), com.app.court.ui.views.AutoCompleteProfileLocation.this);
                    final AutocompletePrediction item = mAutoCompleteAdapter.getItem(position);
                    if (item != null) {
                        final String placeId = item.getPlaceId();
                        PendingResult<PlaceBuffer> placeResult =
                                Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
                    }
                }
            };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback =
            new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(@NonNull PlaceBuffer places) {
                    if (!places.getStatus().isSuccess()) {
                        places.release();
                        return;
                    }
                    if (places.getCount() > 0) {
                        // Got place details
                        final Place place = places.get(0);
                        if (mAutoCompleteLocationListener != null) {
                            mAutoCompleteLocationListener.onItemSelected(place);
                        }
                        // Do your stuff
                    } else {
                        // No place details
                        Toast.makeText(getContext(), "Place details not found.", Toast.LENGTH_LONG).show();
                    }

                    places.release();
                }
            };

}
