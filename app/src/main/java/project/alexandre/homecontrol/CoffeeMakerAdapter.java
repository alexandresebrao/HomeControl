package project.alexandre.homecontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Alexandre on 2015-03-27.
 */
public class CoffeeMakerAdapter extends ArrayAdapter<CoffeeMakerItem> {
    private ArrayList<CoffeeMakerItem> coffeemaker;

    private CoffeeMakerAdapterCallback callback;

    public CoffeeMakerAdapter(Context context, int textViewResourceId, ArrayList<CoffeeMakerItem> coffeemaker) {
        super(context, textViewResourceId, coffeemaker);
        this.coffeemaker = coffeemaker;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listcoffeelayout, null);
        }

        final CoffeeMakerItem item = coffeemaker.get(position);
        if (item != null) {
            TextView identity = (TextView) v.findViewById(R.id.identity);
            Button btnDelete = (Button) v.findViewById(R.id.btnDeleteCoffeeMachine);
            Button btnMakecoffee = (Button) v.findViewById(R.id.btnMakeCoffeee);
            TextView description = (TextView) v.findViewById(R.id.lblCoffeedescription);

            ImageView imgCoffee = (ImageView) v.findViewById(R.id.imgCoffeMakingIcon);

            if (description != null){
                description.setText("Time to prepare: " + item.time());
            }
            if (identity != null) {
                identity.setText(item.identity);
            }

            if (btnDelete != null){
                btnDelete.setTag(item.id);
            }

            if ((imgCoffee != null) && item.inUse){
                imgCoffee.setImageResource(R.drawable.ic_coffee_icon_red);
            }
            if ((imgCoffee != null) && !item.inUse){
                imgCoffee.setImageResource(R.drawable.ic_coffee_icon_green);
            }
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(callback != null) {

                        callback.deletePressed(item.id);
                    }
                }

            });

            btnMakecoffee.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!item.inUse){
                        if(callback != null) {

                            callback.makeCoffeePressed(item.identity,item.id,item.totalTime());
                        }

                    } else {
                        Toast.makeText(getContext(),"Machine in use",Toast.LENGTH_SHORT).show();
                    }

                }

            });
        }


        return v;
    }

    public void setCallback(CoffeeMakerAdapterCallback callback){

        this.callback = callback;
    }

    public interface CoffeeMakerAdapterCallback {

        public void deletePressed(String position);
        public void makeCoffeePressed(String identity, String id, int totaltime);
    }
}




