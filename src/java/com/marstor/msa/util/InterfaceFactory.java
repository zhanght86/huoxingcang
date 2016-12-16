/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.util;

import com.marstor.msa.ba.web.MsaBAInterface;
import com.marstor.msa.ba.web.impl.MsaBAInterfaceDebug;
import com.marstor.msa.ba.web.impl.MsaBAInterfaceImpl;
import com.marstor.msa.cdp.web.MsaCDPInterface;
import com.marstor.msa.cdp.web.impl.MsaCDPInterfaceDebug;
import com.marstor.msa.cdp.web.impl.MsaCDPInterfaceImpl;
import com.marstor.msa.common.bean.SystemUserInformation;
import com.marstor.msa.common.web.CommonInterface;
import com.marstor.msa.common.web.DiskInterface;
import com.marstor.msa.common.web.SCSIInterface;
import com.marstor.msa.common.web.VolumeInterface;
import com.marstor.msa.common.web.ZFSInterface;
import com.marstor.msa.common.web.impl.CommonInterfaceDebug;
import com.marstor.msa.common.web.impl.CommonInterfaceImpl;
import com.marstor.msa.common.web.impl.DiskInterfaceDebug;
import com.marstor.msa.common.web.impl.DiskInterfaceImpl;
import com.marstor.msa.common.web.impl.SCSIInterfaceDebug;
import com.marstor.msa.common.web.impl.SCSIInterfaceImpl;
import com.marstor.msa.common.web.impl.VolumeInterfaceDebug;
import com.marstor.msa.common.web.impl.VolumeInterfaceImpl;
import com.marstor.msa.common.web.impl.ZFSInterfaceDebug;
import com.marstor.msa.common.web.impl.ZFSInterfaceImpl;
import com.marstor.msa.dtrace.web.MonitorInterface;
import com.marstor.msa.dtrace.web.impl.MonitorInterfaceDebug;
import com.marstor.msa.dtrace.web.impl.MonitorInterfaceImpl;
import com.marstor.msa.mdu.web.MsaMDUInterface;
import com.marstor.msa.mdu.web.impl.MsaMDUInterfaceImpl;
import com.marstor.msa.nas.web.NASInterface;
import com.marstor.msa.nas.web.impl.NASInterfaceDebug;
import com.marstor.msa.nas.web.impl.NASInterfaceImpl;
import com.marstor.msa.oracle.web.OracleInterface;
import com.marstor.msa.oracle.web.impl.OracleInterfaceDebug;
import com.marstor.msa.oracle.web.impl.OracleInterfaceImpl;
import com.marstor.msa.sync.web.MsaSYNCInterface;
import com.marstor.msa.sync.web.impl.MsaSYNCInterfaceDebug;
import com.marstor.msa.sync.web.impl.MsaSYNCInterfaceImpl;
import com.marstor.msa.vbox.web.MsaVMInterface;
import com.marstor.msa.vbox.web.impl.MsaVMInterfaceDebug;
import com.marstor.msa.vbox.web.impl.MsaVMInterfaceImpl;
import com.marstor.msa.vtl.web.VtlInterface;
import com.marstor.msa.vtl.web.impl.VtlInterfaceDebug;
import com.marstor.msa.vtl.web.impl.VtlInterfaceImpl;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
public class InterfaceFactory {

    public static boolean bDebug = false;

    static
    {
        if (System.getProperty("os.name").contains("Windows"))
        {
            System.out.println("this is Windows");
            bDebug = true;
        }
    }

    public static String getUser()
    {
        String user;
        ExternalContext exContext;
        try
        {
            exContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) exContext.getRequest();
            SystemUserInformation userinfo = (SystemUserInformation) request.getSession().getAttribute("user");
            if (userinfo == null)
            {
                user = "";
            }
            else
            {
                user = userinfo.getName();
            }
        }
        catch (NullPointerException ex)
        {
            user = "";
        }
        return user;
    }

    public static CommonInterface getCommonInterfaceInstance()
    {
        CommonInterface common;
        if (bDebug)
        {
            common = CommonInterfaceDebug.getInstance();
        }
        else
        {
            common = CommonInterfaceImpl.getInstance();
        }
        common.setUser(getUser());
        return common;
    }
    
    public static MonitorInterface getMonitorInterfaceInstance()
    {
        MonitorInterface monitor;
        if (bDebug)
        {
            monitor = MonitorInterfaceDebug.getInstance();
        }
        else
        {
            monitor = MonitorInterfaceImpl.getInstance();
        }
        monitor.setUser(getUser());
        return monitor;
    }

    public static VolumeInterface getVolumeInterfaceInstance()
    {
        VolumeInterface volume;
        if (bDebug)
        {
            volume = VolumeInterfaceDebug.getInstance();
        }
        else
        {
            volume = VolumeInterfaceImpl.getInstance();
        }
        volume.setUser(getUser());
        return volume;
    }

    public static SCSIInterface getSCSIInterfaceInstance()
    {
        SCSIInterface scsi;
        if (bDebug)
        {
            scsi = SCSIInterfaceDebug.getInstance();
        }
        else
        {
            scsi = SCSIInterfaceImpl.getInstance();
        }
        scsi.setUser(getUser());
        return scsi;

    }

    public static MsaVMInterface getVMInterfaceInstance()
    {

        MsaVMInterface vm;
        if (bDebug)
        {
            vm = MsaVMInterfaceDebug.getInstance();
        }
        else
        {
            vm = MsaVMInterfaceImpl.getInstance();
        }
        vm.setUser(getUser());
        return vm;
    }

    public static MsaCDPInterface getCDPInterfaceInstance()
    {
        MsaCDPInterface cdp;
        if (bDebug)
        {
            cdp = MsaCDPInterfaceDebug.getInstance();
        }
        else
        {
            cdp = MsaCDPInterfaceImpl.getInstance();
        }
        cdp.setUser(getUser());
        return cdp;
    }    

    
    

    public static MsaBAInterface getMBAInterfaceInstance()
    {
        MsaBAInterface mba;
        if (bDebug)
        {
            mba = MsaBAInterfaceDebug.getInstance();
        }
        else
        {
            mba = MsaBAInterfaceImpl.getInstance();
        }
        mba.setUser(getUser());
        return mba;
    }

    public static OracleInterface getOracleInterfaceInstance()
    {
        OracleInterface oracle;
        if (bDebug)
        {
            oracle = OracleInterfaceDebug.getInstance();
        }
        else
        {
            oracle = OracleInterfaceImpl.getInstance();
        }
        oracle.setUser(getUser());
        return oracle;
    }

    public static ZFSInterface getZFSInterfaceInstance()
    {
        ZFSInterface zfs;
        if (bDebug)
        {
            zfs = ZFSInterfaceDebug.getInstance();
        }
        else
        {
            zfs = ZFSInterfaceImpl.getInstance();
        }
        zfs.setUser(getUser());
        return zfs;
    }

    public static DiskInterface getDiskInterfaceInstance()
    {
        DiskInterface disk;
        if (bDebug)
        {
            disk = DiskInterfaceDebug.getInstance();
        }
        else
        {
            disk = DiskInterfaceImpl.getInstance();
        }
        disk.setUser(getUser());
        return disk;
    }

    public static NASInterface getNASInterfaceInstance()
    {
        NASInterface nas;
        if (bDebug)
        {
            nas = NASInterfaceDebug.getInstance();
        }
        else
        {
            nas = NASInterfaceImpl.getInstance();
        }
        nas.setUser(getUser());
        return nas;
    }

    public static MsaSYNCInterface getSYNCInterfaceInstance()
    {
        MsaSYNCInterface sync;
        if (bDebug)
        {
            sync = MsaSYNCInterfaceDebug.getInstance();
        }
        else
        {
            sync = MsaSYNCInterfaceImpl.getInstance();
        }
        sync.setUser(getUser());
        return sync;
    }

    public static VtlInterface getVtlInterfaceInstance()
    {
        VtlInterface vtl;
        if (bDebug)
        {
            vtl = VtlInterfaceDebug.getInstance();
        }
        else
        {
            vtl = VtlInterfaceImpl.getInstance();
        }
        vtl.setUser(getUser());
        return vtl;
    }
    
    public static MsaMDUInterface getMsaMDUInterfaceInstance()
    {
        MsaMDUInterface mdu;
        if (bDebug)
        {
            mdu = MsaMDUInterfaceImpl.getInstance();  //¼Ù½Ó¿Ú
        }
        else
        {
            mdu = MsaMDUInterfaceImpl.getInstance();
        }
        return mdu;
    }
}
