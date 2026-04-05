package hendee.dndcharactermanager;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greg on 9/22/2017.
 */

public class AddFeat extends DialogFragment {
    protected Spinner featSpinner;
    protected Button nextButton;
    protected TextView features;
    protected List<String> featModifiers;
    protected List<String> featTraits;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        Dialog dialog = new Dialog(getActivity());
        View view = getView().findViewById(R.id.addfeatscrollview);
        dialog.setContentView(view);

        featSpinner = (Spinner) dialog.findViewById(R.id.feats);
        nextButton = dialog.findViewById(R.id.choosefeatcomplete);
        features = (TextView) dialog.findViewById(R.id.featinfo);

        final Feats f = new Feats(getContext());
        ArrayList<String> featsList = new ArrayList<>();

        try {
            featsList = f.getFeats();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> featsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, featsList);
        featsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        featSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                loadFeatures();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing
            }
        });
    }

    public void loadFeatures()
    {
        String traits = loadTraits();
        features.setText(traits);
    }

    public String loadTraits()
    {
        final Feats f = new Feats(getContext());
        String featuresOutput = "";
        try {
            featTraits = f.getFeatures(featSpinner.getSelectedItem().toString());
            for (int i = 0; i < featTraits.size(); i++) {
                featuresOutput += featTraits.get(i) + "\n\n";
            }
            //features.setText(featuresOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  featuresOutput;
    }

}
