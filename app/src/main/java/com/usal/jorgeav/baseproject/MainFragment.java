package com.usal.jorgeav.baseproject;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.usal.jorgeav.baseproject.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment {
    public static final String EDITTEXT_FUNCION_KEY = "edittex-funcion-key";
    public static final String EDITTEXT_NONI_KEY = "edittex-noni-key";

    @BindView(R.id.et_funcion)
    EditText etFuncion;
    @BindView(R.id.et_noni)
    EditText etNoNi;
    @BindView(R.id.bt_quinemc)
    Button btQuineMc;
    @BindView(R.id.constraint_resultados)
    ConstraintLayout constraintResultados;
    @BindView(R.id.tv_sumatorio)
    TextView tvSumatorio;
    @BindView(R.id.tv_funcion_minterm)
    TextView tvFuncionMinterm;
    @BindView(R.id.tv_puertas_minterm)
    TextView tvPuertasMinterm;
    @BindView(R.id.bt_detalles_minterm)
    Button btDetallesMinterm;
    @BindView(R.id.tv_producto)
    TextView tvProducto;
    @BindView(R.id.tv_funcion_maxterm)
    TextView tvFuncionMaxterm;
    @BindView(R.id.tv_puertas_maxterm)
    TextView tvPuertasMaxterm;
    @BindView(R.id.bt_detalles_maxterm)
    Button btDetallesMaxterm;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_error)
    TextView tvError;
    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            etFuncion.setText(savedInstanceState.getString(EDITTEXT_FUNCION_KEY));
            etNoNi.setText(savedInstanceState.getString(EDITTEXT_NONI_KEY));
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EDITTEXT_FUNCION_KEY, etFuncion.getText().toString());
        outState.putString(EDITTEXT_NONI_KEY, etNoNi.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        Bundle b = getArguments();
        if (b != null) {
            etFuncion.setText(b.getString(MainActivity.BUNDLE_ET_FUNCION_KEY));
            etNoNi.setText(b.getString(MainActivity.BUNDLE_ET_NONI_KEY));
        }

        etFuncion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mListener != null)
                    mListener.setEditTextFuncion(editable.toString());
            }
        });
        etNoNi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mListener != null)
                    mListener.setEditTextNoni(editable.toString());
            }
        });

        btQuineMc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quineMccluskey(view);
            }
        });
        btDetallesMinterm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onDetallesMinterm();
            }
        });
        btDetallesMaxterm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onDetallesMaxterm();
            }
        });

        return view;
    }

    public void quineMccluskey(View view) {
        showProgressBar();

        if (!(etNoNi.getText().toString().isEmpty() || etNoNi.getText().toString().matches(Utils.PATTERN_LISTA_TERMINOS))) {
            showError("No se reconoce la sintaxis de los terminos NO/NI");
        } else {
            if (mListener != null) {
                mListener.onQuineMcluskey(etFuncion.getText().toString(), etNoNi.getText().toString());
            } else {
                showError("Error en el fragment");
            }
        }
    }

    public void setMintermResults(String minterms, String funcionSimple, int puertas) {
        tvSumatorio.setText(String.format(getResources().getString(R.string.sumatorio), minterms));
        tvFuncionMinterm.setText(String.format(getResources().getString(R.string.funcion), funcionSimple));
        tvPuertasMinterm.setText(String.format(getResources().getString(R.string.puertas), puertas));
        showResultados();
    }

    public void setMaxtermResults(String maxterms, String funcionSimple, int puertas) {
        tvProducto.setText(String.format(getResources().getString(R.string.producto), maxterms));
        tvFuncionMaxterm.setText(String.format(getResources().getString(R.string.funcion), funcionSimple));
        tvPuertasMaxterm.setText(String.format(getResources().getString(R.string.puertas), puertas));
        showResultados();
    }

    public void showError(String error) {
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(error);
        progressBar.setVisibility(View.INVISIBLE);
        constraintResultados.setVisibility(View.INVISIBLE);

    }

    public void showProgressBar() {
        tvError.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        constraintResultados.setVisibility(View.INVISIBLE);

    }

    public void showResultados() {
        tvError.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        constraintResultados.setVisibility(View.VISIBLE);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(false);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onQuineMcluskey(String funcion, String noni);
        void onDetallesMinterm();
        void onDetallesMaxterm();
        void setEditTextFuncion(String string);
        void setEditTextNoni(String string);
    }
}
