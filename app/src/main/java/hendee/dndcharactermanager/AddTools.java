package hendee.dndcharactermanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import static android.R.attr.duration;

/**
 * Created by Greg on 9/20/2017.
 */

public class AddTools extends DialogFragment {

    int selectedCount = 0;
    character currentChar;
    ArrayList<String> concreteChoices = new ArrayList<String>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //final character currentChar = (character) getArguments().getSerializable("character");
        ArrayList<String> tools = null;

        final int maxCount = getArguments().getInt("maxCount");
        final String toolSubSet = getArguments().getString("toolSubSet");
        try {
            tools = getTools(currentChar, toolSubSet);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        final ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final String[] toolStringArray = new String[tools.size()];

        for (int i =0; i< tools.size(); i++){
            toolStringArray[i] = tools.get(i);
        }

        // Set the dialog title
        builder.setTitle("Add additional Tool(s)")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(toolStringArray, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    if (selectedCount < maxCount) {
                                        mSelectedItems.add(toolStringArray[which].toString());
                                        selectedCount++;
                                    }
                                    else {
                                        Toast toast = Toast.makeText(getActivity(), "You've already selected" + maxCount + "tool(s)", Toast.LENGTH_SHORT);
                                        toast.show();
                                        ((AlertDialog) dialog).getListView().setItemChecked(which, false);
                                    }
                                } else if (mSelectedItems.contains(toolStringArray[which].toString())) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                    selectedCount --;
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        for (int i = 0; i< mSelectedItems.size(); i++)
                            currentChar.toolProficiencies.add(mSelectedItems.get(i).toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
    public ArrayList<String> getTools(character currentChar, String toolSubSet) throws XPathExpressionException {
        final ArrayList<String> possibleTools = new ArrayList<String>();
        if (concreteChoices.isEmpty())
        {
            NodeList tools;
            AssetManager assetManager = getActivity().getAssets();
            InputStream inputStream = null;
            try {
                inputStream = assetManager.open(toolSubSet + ".xml");
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            String xpathExpression = "tools/tool";

            tools = (NodeList) xPath.evaluate(xpathExpression, new InputSource(inputStream), XPathConstants.NODESET);
            for (int i = 0; i < tools.getLength(); i++) {
                possibleTools.add(tools.item(i).getFirstChild().getNodeValue());
            }
        }
        else
        {
            possibleTools.addAll(concreteChoices);
        }
        for(int i = 0; i<currentChar.toolProficiencies.size(); i++)
            possibleTools.remove(currentChar.toolProficiencies.get(i));
        return possibleTools;
    }

    public void setIncomingChar(character incoming)
    {
        currentChar = incoming;
    }

    public void setConcreteChoices(ArrayList<String> choices)
    {
        concreteChoices = choices;
    }
}