/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.restful.common.bean;

import com.marstor.msa.common.bean.SystemInformation;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Li Mengyang <li.mengyang@marstor.com>
 */
@XmlRootElement
public class SystemInfo {
    private String host_name;//主机名
    private String user_name;//用户名称
    private String kernel_version;//操作系统版本
    private String os_version;//操作系统版本
    private String processor_type;//处理器型号
    private String processor_count;//处理器型号
    private String supported_protocol;//支持协议
    private String service_status;//服务状态 启动/关闭
    private String access_permission;//访问权限 读写/只读
    private String[] network_addresses;//所有网卡IP
    private String system_uptime;//系统运行时间
    private String version;//版本信息
    private String manufacturer;//制造商
    private String memory_capacity; //内存大小
    private String[] wwpn; //光纤卡WWPN
    private String system_key;

    public SystemInfo() {
    }

    public SystemInfo(SystemInformation data) {
        this.host_name = data.hostname;
        this.user_name = data.username;
        this.kernel_version = data.kernel;
        this.os_version = data.os;
        this.processor_type = data.cpu;
        this.processor_count = data.cpuCount;
        this.supported_protocol = data.agreement;
        this.service_status = data.serverFlag;
        this.access_permission = data.accessing;
        this.network_addresses = data.netConfig;
        this.system_uptime = data.usedtime;
        this.version = data.version;
        this.manufacturer = data.manufacturer;
        this.memory_capacity = data.memory;
        this.wwpn = data.wwpn;
        this.system_key = data.systemKey;
    }
    
    

    public String getHost_name() {
        return host_name;
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getKernel_version() {
        return kernel_version;
    }

    public void setKernel_version(String kernel_version) {
        this.kernel_version = kernel_version;
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version;
    }

    public String getProcessor_type() {
        return processor_type;
    }

    public void setProcessor_type(String processor_type) {
        this.processor_type = processor_type;
    }

    public String getProcessor_count() {
        return processor_count;
    }

    public void setProcessor_count(String processor_count) {
        this.processor_count = processor_count;
    }

    public String getSupported_protocol() {
        return supported_protocol;
    }

    public void setSupported_protocol(String supported_protocol) {
        this.supported_protocol = supported_protocol;
    }

    public String getService_status() {
        return service_status;
    }

    public void setService_status(String service_status) {
        this.service_status = service_status;
    }

    public String getAccess_permission() {
        return access_permission;
    }

    public void setAccess_permission(String access_permission) {
        this.access_permission = access_permission;
    }

    public String[] getNetwork_addresses() {
        return network_addresses;
    }

    public void setNetwork_addresses(String[] network_addresses) {
        this.network_addresses = network_addresses;
    }

    public String getSystem_uptime() {
        return system_uptime;
    }

    public void setSystem_uptime(String system_uptime) {
        this.system_uptime = system_uptime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getMemory_capacity() {
        return memory_capacity;
    }

    public void setMemory_capacity(String memory_capacity) {
        this.memory_capacity = memory_capacity;
    }

    public String[] getWwpn() {
        return wwpn;
    }

    public void setWwpn(String[] wwpn) {
        this.wwpn = wwpn;
    }

    public String getSystem_key() {
        return system_key;
    }

    public void setSystem_key(String system_key) {
        this.system_key = system_key;
    }
    
    
    
    
}
