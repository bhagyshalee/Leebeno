package leebeno.com.leebeno.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Adapters.WalletHistoryAdapter;
import leebeno.com.leebeno.R;

public class WalletHistory extends Fragment {
    @Bind(R.id.walletRecycler)
    RecyclerView walletRecycler;

    List<String> listJobItemTitle;
    List<String> listJobItemPrice;
    List<String> listJobItemTraId;
    List<String> listJobItemDebitFrom;
    List<String> listJobItemDebitOn;
    WalletHistoryAdapter bankHistoryAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wallet_history, container, false);
        ButterKnife.bind(this, v);
        listJobItemTitle = new ArrayList<>();
        listJobItemPrice = new ArrayList<>();
        listJobItemTraId = new ArrayList<>();
        listJobItemDebitFrom = new ArrayList<>();
        listJobItemDebitOn = new ArrayList<>();
        walletRecycler.setHasFixedSize(true);
        walletRecycler.setNestedScrollingEnabled(true);
        walletRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        listJobItemTitle.add("Carpenter for house");
        listJobItemPrice.add("$ 500");
        listJobItemTraId.add("gref refe rgrgre g regre g regreg re gre gre g regreg regreg");
        listJobItemDebitFrom.add("Debit To richi");
        listJobItemDebitOn.add("10 sept 2018");
        listJobItemTitle.add("Carpenter for house");
        listJobItemPrice.add("$ 500");
        listJobItemTraId.add("gref refe rgrgre g regre g regreg re gre gre g regreg regreg");
        listJobItemDebitFrom.add("Debit To richi");
        listJobItemDebitOn.add("10 sept 2018");
        listJobItemTitle.add("Carpenter for house");
        listJobItemPrice.add("$ 500");
        listJobItemTraId.add("gref refe rgrgre g regre g regreg re gre gre g regreg regreg");
        listJobItemDebitFrom.add("Debit To richi");
        listJobItemDebitOn.add("10 sept 2018");
        listJobItemTitle.add("Carpenter for house");
        listJobItemPrice.add("$ 500");
        listJobItemTraId.add("gref refe rgrgre g regre g regreg re gre gre g regreg regreg");
        listJobItemDebitFrom.add("Debit To richi");
        listJobItemDebitOn.add("10 sept 2018");
        listJobItemTitle.add("Carpenter for house");
        listJobItemPrice.add("$ 500");
        listJobItemTraId.add("gref refe rgrgre g regre g regreg re gre gre g regreg regreg");
        listJobItemDebitFrom.add("Debit To richi");
        listJobItemDebitOn.add("10 sept 2018");
        listJobItemTitle.add("Carpenter for house");
        listJobItemPrice.add("$ 500");
        listJobItemTraId.add("gref refe rgrgre g regre g regreg re gre gre g regreg regreg");
        listJobItemDebitFrom.add("Debit To richi");
        listJobItemDebitOn.add("10 sept 2018");



        bankHistoryAdapter = new WalletHistoryAdapter(listJobItemTitle, getActivity(), listJobItemPrice, listJobItemTraId, listJobItemDebitFrom, listJobItemDebitOn);
        walletRecycler.setAdapter(bankHistoryAdapter);


        return v;
    }
}