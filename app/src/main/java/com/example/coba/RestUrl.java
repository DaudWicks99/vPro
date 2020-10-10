package com.example.coba;

public class RestUrl {

    public static final String BASE_IMAGE_URL ="/common/uploads/";
    public static final String IMAGE_URL_VOTING ="ListMenuPic/low/";
    public static final String IMAGE_URL_INFO = "InfoListMenuPic/low/";
    public static final String IMAGE_URL_PROFILE = "ProfilePic/low/";
    public static final String REGISTER ="/v-project/register";
    public static final String LOGIN ="/v-project/login";
    public static final String PROFILE ="/v-project/profile";
    public static final String AMBIL_MENU ="/v-project/ambil-menu";
    public static final String ADD_LIST_MENU ="/v-project/add-list-menu";
    public static final String DELETE_LIST_MENU ="/v-project/delete-list-menu";
    public static final String UPDATE_LIST_MENU ="/v-project/update-list-menu";
    public static final String AMBIL_VOTE ="/v-project/ambil-vote";
    public static final String AMBIL_SATU_VOTE ="/v-project/ambil-satu-vote";
    public static final String ADD_VOTE ="/v-project/add-vote";
    public static final String UPDATE_VOTE ="/v-project/update-vote";
    public static final String DELETE_VOTE ="/v-project/delete-vote";
    public static final String HASIL_VOTE ="/v-project/hasil-vote";
    public static final String CHECK_ADMIN ="/v-project/check-admin";
    public static final String CHECK_TOKEN ="/v-project/check-token";
    public static final String ADD_INFO_MENU ="/v-project/add-info-menu";
    public static final String AMBIL_INFO ="/v-project/ambil-info";
    public static final String AMBIL_SATU_INFO ="/v-project/ambil-satu-info";
    public static final String AMBIL_SATU_HASIL ="/v-project/ambil-satu-hasil";
    public static final String AMBIL_LIST_HASIL ="/v-project/ambil-list-hasil";
    public static final String SUBMIT_VOTE ="/v-project/hasil-vote";
    public static final String DELETE_INFO ="/v-project/delete-info";
    public static final String UPDATE_INFO ="/v-project/update-info";
    public static final String UPDATE_PIC_INFO ="/v-project/update-pic-info";
    public static final String UPDATE_PIC_LIST ="/v-project/update-pic-list";
    public static final String UPDATE_PIC_PROFILE ="/v-project/update-pic-profile";
    public static final String ADD_PROFILE ="/v-project/add-profile";
    public static final String BIODATA ="/v-project/biodata";

    public static String getUrl(String name){
        if (name!=null){

            if (BuildConfig.DEBUG){
                return "http://167.71.199.106:3000/dev"+name;
            }
            return "http://167.71.199.106:3000/v1"+name;
        }
        return "";
    }

    public static String getImgBase(String name){
        if (BuildConfig.DEBUG){
            return "http://167.71.199.106:3000/common/uploads/dev/"+name;
        }
        return "http://167.71.199.106:3000/common/uploads/prod/"+name;
    }
    public static String getSubscribtion(){
        if (BuildConfig.DEBUG){
            return "admindev";
        }
        return "admin";
    }


}
