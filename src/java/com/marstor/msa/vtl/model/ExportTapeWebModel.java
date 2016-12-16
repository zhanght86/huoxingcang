/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.vtl.model;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Administrator
 */
public class ExportTapeWebModel  extends ListDataModel<ExportTapeWeb> implements SelectableDataModel<ExportTapeWeb>, Serializable {

    /**
     * Creates a new instance of ExportTapeWebModel
     */
    public ExportTapeWebModel() {
    }
  public ExportTapeWebModel(List<ExportTapeWeb> data) {
        super(data);
    }
    @Override
    public Object getRowKey(ExportTapeWeb t) {
         return t.getName();
    }

    @Override
    public ExportTapeWeb getRowData(String rowKey) {
        List<ExportTapeWeb> tapes = (List<ExportTapeWeb>) getWrappedData();

        for (ExportTapeWeb tape : tapes) {
            if (tape.getName().equals(rowKey)) {
                return tape;
            }
        }
        return null;
    }
}
