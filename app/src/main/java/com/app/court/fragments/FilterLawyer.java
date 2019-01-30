package com.app.court.fragments;

import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.court.R;
import com.app.court.entities.EntitySpinnerListing;
import com.app.court.fragments.abstracts.BaseFragment;
import com.app.court.global.WebServiceConstants;
import com.app.court.helpers.InternetHelper;
import com.app.court.helpers.UIHelper;
import com.app.court.interfaces.IGetLocation;
import com.app.court.ui.dialogs.DialogFactory;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.AutoCompleteLocation;
import com.app.court.ui.views.CustomRatingBar;
import com.app.court.ui.views.TitleBar;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FilterLawyer extends BaseFragment implements IGetLocation, View.OnClickListener {

    @BindView(R.id.sp_affiliation)
    Spinner spAffiliation;
    @BindView(R.id.sp_specialization)
    Spinner spSpecialization;
    @BindView(R.id.rating_bar)
    CustomRatingBar ratingBar;
    @BindView(R.id.sp_language)
    Spinner spLanguage;
    @BindView(R.id.tv_location)
    AnyTextView tvLocation;
    Unbinder unbinder;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_autocomplete)
    AutoCompleteLocation txtAutoComplete;

    private ArrayList<String> affiliationList = new ArrayList<>();
    private ArrayList<Integer> affiliationListIds = new ArrayList<>();
    private String affiliationLevel;
    private String affiliationId;
    private ArrayList<String> specializationList = new ArrayList<>();
    private ArrayList<Integer> specializationListIds = new ArrayList<>();
    private String specializationLevel;
    private String specializationId;
    private ArrayList<String> languageList = new ArrayList<>();
    private ArrayList<Integer> languageListIds = new ArrayList<>();
    private String languageLevel;
    private String languageId;
    private LatLng location;
    private String AFFILIATION_ID = "";
    private String LANGUAGE_ID = "";
    private String SPECIALIZATION_ID = "";
    private String LOCATION = "";
    private String LAT = "";
    private String LONG = "";
    private String RATING = "";
    private boolean task_completed = false;

    public static FilterLawyer newInstance() {
        Bundle args = new Bundle();

        FilterLawyer fragment = new FilterLawyer();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_lawyer, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ratingBar.setOnScoreChanged(new CustomRatingBar.IRatingBarCallbacks() {
            @Override
            public void scoreChanged(float score) {
                if (score < 1.0f)
                    ratingBar.setScore(1.0f);
            }
        });

        getAffiliation();
        getSpecialization();
        getLanguage();
        setAutoCompleteListner();
        tvLocation.setOnClickListener(this);

        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    languageId = String.valueOf(languageListIds.get(position));
                    LANGUAGE_ID = languageId;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spSpecialization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    specializationId = String.valueOf(specializationListIds.get(position));
                    SPECIALIZATION_ID = specializationId;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spAffiliation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    affiliationId = String.valueOf(affiliationListIds.get(i));
                    AFFILIATION_ID = affiliationId;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                SPECIALIZATION_ID = "";
                AFFILIATION_ID = "";
                LANGUAGE_ID = "";
            }
        }, 1000);

    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.filter));
        titleBar.showResetButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilter();
            }
        });
    }

    @Override
    public void onLocationSet(LatLng location, String formattedAddress) {
        tvLocation.setText(formattedAddress + "");

      /*  this.location = location;
        LOCATION = tvLocation.getText().toString();
        LAT = String.valueOf(location.latitude);
        LONG = String.valueOf(location.longitude);*/
    }

    private void setAutoCompleteListner() {

        txtAutoComplete.setAutoCompleteTextListener(new AutoCompleteLocation.AutoCompleteLocationListener() {
            @Override
            public void onTextClear() {

            }

            @Override
            public void onItemSelected(Place selectedPlace) {
                LOCATION = selectedPlace.getAddress().toString();
                LAT = String.valueOf(selectedPlace.getLatLng().latitude);
                LONG = String.valueOf(selectedPlace.getLatLng().longitude);
                UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                if (getDockActivity().getWindow() != null)
                    getDockActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });

    }


    @Override
    public void onClick(View v) {

        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            MapControllerFragment mapControllerFragment = MapControllerFragment.newInstance();
            mapControllerFragment.setDelegate(this);
            DialogFactory.showMapControllerDialog(getDockActivity(), mapControllerFragment);
        }
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (!String.valueOf(Math.round(ratingBar.getScore())).equals("0")) {
            RATING = String.valueOf(Math.round(ratingBar.getScore()));
        }

        getDockActivity().popFragment();
        getDockActivity().replaceDockableFragment(FindLawyerFragment.newInstance(AFFILIATION_ID, SPECIALIZATION_ID, LANGUAGE_ID,
                RATING, LOCATION, LAT, LONG), "FindLawyerFragment");
    }

    private void getLanguage() {
        serviceHelper.enqueueCall(webServiceWithoutToken.getLanguage(), WebServiceConstants.LANGUAGE);
    }

    private void getSpecialization() {
        serviceHelper.enqueueCall(webServiceWithoutToken.getSpecialization(), WebServiceConstants.SPECIALIZATION);
    }

    private void getAffiliation() {
        serviceHelper.enqueueCall(webService.getAffilation(), WebServiceConstants.AFFILIATION);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.LANGUAGE:

                languageList = new ArrayList<>();
                ArrayList<EntitySpinnerListing> languageEnt = new ArrayList<>();

                EntitySpinnerListing entitySpinnerListing = new EntitySpinnerListing();
                entitySpinnerListing.setTitle("Select Language");
                entitySpinnerListing.setArTitle("Select Language");
                entitySpinnerListing.setId(0);

                languageEnt.add(entitySpinnerListing);
                languageEnt.addAll((ArrayList<EntitySpinnerListing>) result);

                for (EntitySpinnerListing ent : languageEnt) {
                    languageList.add(ent.getTitle());
                    languageListIds.add(ent.getId());
                }
                // ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, languageList);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getDockActivity()
                        , android.R.layout.simple_spinner_item, languageList) {
                    @Override
                    public boolean isEnabled(int position) {
                        return !(position == 0);
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                        return view;
                    }

                };
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
                spLanguage.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;

            case WebServiceConstants.SPECIALIZATION:

                specializationList = new ArrayList<>();
                ArrayList<EntitySpinnerListing> specializationEnt = new ArrayList<>();
                EntitySpinnerListing specializationListing = new EntitySpinnerListing();
                specializationListing.setTitle("Select Specialization");
                specializationListing.setArTitle("Select Specialization");
                specializationListing.setId(0);

                specializationEnt.add(specializationListing);
                specializationEnt.addAll((ArrayList<EntitySpinnerListing>) result);

                for (EntitySpinnerListing ent : specializationEnt) {
                    specializationList.add(ent.getTitle());
                    specializationListIds.add(ent.getId());
                }
                //    ArrayAdapter<String> adapterSpecialization = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, specializationList);

                ArrayAdapter<String> adapterSpecialization = new ArrayAdapter<String>(getDockActivity()
                        , android.R.layout.simple_spinner_item, specializationList) {
                    @Override
                    public boolean isEnabled(int position) {
                        return !(position == 0);
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                        return view;
                    }

                };
                adapterSpecialization.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
                spSpecialization.setAdapter(adapterSpecialization);
                adapterSpecialization.notifyDataSetChanged();
                break;

            case WebServiceConstants.AFFILIATION:

                affiliationList = new ArrayList<>();
                ArrayList<EntitySpinnerListing> affiliationEnt = new ArrayList<>();
                EntitySpinnerListing affiliationListing = new EntitySpinnerListing();
                affiliationListing.setTitle("Select Affiliation");
                affiliationListing.setArTitle("Select Affiliation");
                affiliationListing.setId(0);

                affiliationEnt.add(affiliationListing);
                affiliationEnt.addAll((ArrayList<EntitySpinnerListing>) result);

                for (EntitySpinnerListing ent : affiliationEnt) {
                    affiliationList.add(ent.getTitle());
                    affiliationListIds.add(ent.getId());
                }
                //     ArrayAdapter<String> adapterAffiliation = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item_2, affiliationList);
                ArrayAdapter<String> adapterAffiliation = new ArrayAdapter<String>(getDockActivity()
                        , android.R.layout.simple_spinner_item, affiliationList) {
                    @Override
                    public boolean isEnabled(int position) {
                        return !(position == 0);
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                        return view;
                    }

                };
                adapterAffiliation.setDropDownViewResource(R.layout.spinner_dropdown_item_2);
                spAffiliation.setAdapter(adapterAffiliation);
                adapterAffiliation.notifyDataSetChanged();
                break;
        }
    }

    private void resetFilter() {
        LAT = "";
        LONG = "";
        LOCATION = "";
        ratingBar.setScore(0);
        RATING = "";
        tvLocation.setText("");
        txtAutoComplete.setText("");
        spLanguage.setSelection(0);
        spAffiliation.setSelection(0);
        spSpecialization.setSelection(0);
        LANGUAGE_ID = "";
        SPECIALIZATION_ID = "";
        AFFILIATION_ID = "";
    }
}
