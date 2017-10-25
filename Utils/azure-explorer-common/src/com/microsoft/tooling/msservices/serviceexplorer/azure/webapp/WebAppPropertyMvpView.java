package com.microsoft.tooling.msservices.serviceexplorer.azure.webapp;

import com.microsoft.azuretools.core.mvp.ui.base.MvpView;
import com.microsoft.azuretools.core.mvp.ui.webapp.WebAppProperty;

public interface WebAppPropertyMvpView extends MvpView {
    public void onLoadWebAppProperty();

    public void showProperty(WebAppProperty property);
}
