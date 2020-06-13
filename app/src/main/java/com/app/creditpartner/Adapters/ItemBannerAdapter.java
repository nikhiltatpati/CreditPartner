package com.app.creditpartner.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app.creditpartner.Activities.IFSCCodeFinderActivity;
import com.app.creditpartner.Activities.CreditReportActivity;
import com.app.creditpartner.Activities.EMICalculatorActivity;
import com.app.creditpartner.Activities.FuelRates;
import com.app.creditpartner.Activities.GoldRates;
import com.app.creditpartner.Models.ItemBannerModel;
import com.app.creditpartner.R;

import java.util.List;

public class ItemBannerAdapter extends PagerAdapter {

    private List<ItemBannerModel> models;
    private LayoutInflater layoutInflater;
    private Context context;
    private String label;

    public ItemBannerAdapter(List<ItemBannerModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.banner_item_bottom, container, false);

        ImageView imageView;
        TextView title, desc;

        imageView = view.findViewById(R.id.image);
        //title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);

        imageView.setImageResource(models.get(position).getImage());
        //title.setText(models.get(position).getTitle());
        desc.setText(models.get(position).getDesc());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(models.get(position).getTitle()=="report"){
                    context.startActivity(new Intent(context, CreditReportActivity.class));
                }
                if(models.get(position).getTitle()=="fuel"){
                    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                    View mView = inflater.inflate(R.layout.dialogbox, null);
                    mBuilder.setTitle("Select State");
                    Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.state));
                    adapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
                    mSpinner.setAdapter(adapter);
                    mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(context, FuelRates.class);
                            intent.putExtra("currentState",mSpinner.getSelectedItem().toString());
                            context.startActivity(intent);

                        }

                    });
                    mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();


                }

                if(models.get(position).getTitle()=="gold"){
                    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                    View mView = inflater.inflate(R.layout.dialogbox, null);
                    mBuilder.setTitle("Select State");
                    Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.state));
                    adapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
                    mSpinner.setAdapter(adapter);

                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {
                            label = parent.getItemAtPosition(position).toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            Toast.makeText(adapterView.getContext(), "Please select something",Toast.LENGTH_LONG).show();

                        }
                    });

                    mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(context, GoldRates.class);
                            intent.putExtra("currentState", label);
                            context.startActivity(intent);

                        }

                    });
                    mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                }


                if(models.get(position).getTitle()=="find"){
                    context.startActivity(new Intent(context, IFSCCodeFinderActivity.class));


                }

                if(models.get(position).getTitle()=="calculator"){
                    Intent intent = new Intent(context, EMICalculatorActivity.class);
                    intent.putExtra("param", models.get(position).getTitle());
                    context.startActivity(intent);

                }
                // finish();
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
