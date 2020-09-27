package com.example.cci.frament;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cci.R;
import com.example.cci.activity.MainActivity;
import com.example.cci.adapter.AdapterProductList;
import com.example.cci.connection.PostClass;
import com.example.cci.data.Tools;
import com.example.cci.model.Product;
import com.google.android.material.tabs.TabLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentHome extends Fragment implements View.OnClickListener {

    private View root_view;
    public static RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private GetProduct gp;
    private String result="1";
    public FragmentActivity _fa;
    private SearchView searchbox;
    public String URL_POST= "";
    public JSONArray jsonArr=null;
    private List<Product> original_productlist;
    private List<Product> filtered_productlist;
    private Product selectedProduct;
    public ProgressDialog pDialog;
    private Context ctx;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_home, null);
        setHasOptionsMenu(true);
        ctx = getContext();

        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (Tools.InternetConnection(getActivity())) {
            try {
                gp=new GetProduct(getActivity());
                gp.execute();
            } catch (Exception e) {
                Toast.makeText(ctx, getString(R.string.products_not_loaded), Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText(ctx, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
        }

        selectedProduct=null;
        searchbox=(SearchView)root_view.findViewById(R.id.searchbox);
        searchbox.onActionViewExpanded();
        searchbox.setIconified(false);
        filtered_productlist = new ArrayList<>();
        searchbox.setQueryHint(getString(R.string.search_product));
        searchbox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                FilterItem(original_productlist,s);
                return false;
            }
        });
        return root_view;
    }

    public class GetProduct extends AsyncTask<String, Object, List<Product>> {

        FragmentActivity fa;
        public GetProduct(FragmentActivity activity) {
            fa=activity;
            _fa=fa;
        }

        @Override
        protected void onPreExecute() {
            try {
                pDialog = new ProgressDialog(_fa.getApplicationContext());
                pDialog.setMessage(getString(R.string.products_loading));
                pDialog.setIndeterminate(true);
                pDialog.setCancelable(true);
                pDialog.show();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        protected List<Product> doInBackground(String... arg) {
            result="1";
            List<Product> items = new ArrayList<>();
            PostClass post = new PostClass();
            URL_POST = "https://anypoint.mulesoft.com/mocking/api/v1/links/c4326100-ed9e-4835-acf6-99435177eba0/json/sf-json?client_id=123456&client_secret=45454787";
            try {
                String json = post.httpPost(URL_POST,"GET");
                if (json != null) {
                    jsonArr = new JSONArray((json));
                    if (jsonArr.length() > 0) {
                        for (int i = 0; i < jsonArr.length(); i++) {
                            try {
                                JSONObject jsonobject = jsonArr.getJSONObject(i);
                                Product product = new Product(jsonobject);
                                items.add(product);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                //Collections.shuffle(items);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            original_productlist=items;
            return items;
        }

        @Override
        protected void onPostExecute(List<Product> str) {
            super.onPostExecute(str);
            /*try {
                pDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            if(str.size()>0)
            {
                Collections.sort(str,new ProductSorter());
                //Collections.reverse(str);
            }
            if(result.equals("1"))
            {
                DisplayProductList(str);
            }
        }
    }

    public void RefreshData(MainActivity activity)
    {
        original_productlist.clear();
        DisplayProductList(original_productlist);
        GetProduct g = new GetProduct(activity);
        g.execute();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_activity_main, menu);
        final MenuItem refresh = menu.findItem(R.id.action_refresh);
        setItemsVisibility(menu, refresh, true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*if(item.getTitle().equals(getString(R.string.title_nav_home))){
            Log.i("XXX","Home");
        } else if (item.getTitle().equals(R.string.title_nav_close)){
            Log.i("XXX","Close");
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    public class ProductSorter implements Comparator<Product> {

        public int compare(Product one, Product another){
            String s1 = one.getName();
            String s2 = another.getName();
            return s1.compareToIgnoreCase(s2);
        }
    }

    private void DialogProductDetails(final Product product) {
        final Dialog dialog = new Dialog(_fa);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_product_details);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView productCode = dialog.findViewById(R.id.code);
        TextView productName = dialog.findViewById(R.id.name);
        TextView productPrice = dialog.findViewById(R.id.price);
        TextView productNumber = dialog.findViewById(R.id.number);
        TextView productCategory = dialog.findViewById(R.id.category);
        TextView productCountry = dialog.findViewById(R.id.countryCode);

        productCode.setText(product.getCode());
        productName.setText(product.getName());
        productPrice.setText(product.getAdvicedPrice());
        productNumber.setText(product.getProductNumber());
        productCategory.setText(product.getCategory());
        productCountry.setText(product.getCountryCode());

        dialog.getWindow().setFormat(PixelFormat.UNKNOWN);
        ((ImageView)dialog.findViewById(R.id.img_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void DisplayProductList(List<Product> items){

        if(items.size()>0)
        {
            AdapterProductList mAdapter = new AdapterProductList(_fa, items.get
                    (items.size()-1), items);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mAdapter.SetOnItemClickListener(new AdapterProductList.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, Product product) {
                    selectedProduct = product;
                    DialogProductDetails(product);
                }
            });
        }
        else
        {
            List<Product> noitem= new ArrayList<>();
            Product p = new Product();
            AdapterProductList mAdapter = new AdapterProductList(_fa, p, noitem);

            recyclerView.setAdapter(mAdapter);
            mAdapter.SetOnItemClickListener(new AdapterProductList.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, Product product) {
                    DialogProductDetails(product);
                }
            });
        }
    }

    private void FilterItem(List<Product> items, String s){
        filtered_productlist= new ArrayList<>();

        if (s.length()==0){
            DisplayProductList(original_productlist);
        } else {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getName().toLowerCase().contains(s.toLowerCase()) || items.get(i).getCode().toLowerCase().contains(s.toLowerCase())) {
                    filtered_productlist.add(items.get(i));
                }
            }
            DisplayProductList(filtered_productlist);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPause(){
        super.onPause();
    }

}