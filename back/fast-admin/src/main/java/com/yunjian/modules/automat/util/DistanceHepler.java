package com.yunjian.modules.automat.util;

public class DistanceHepler {
    private final static double Earth_Radius = 6378.137f;

    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        double x1 = Math.cos(lat1) * Math.cos(lng1);
        double y1 = Math.cos(lat1) * Math.sin(lng1);
        double z1 = Math.sin(lat1);

        double x2 = Math.cos(lat2) * Math.cos(lng2);
        double y2 = Math.cos(lat2) * Math.sin(lng2);
        double z2 = Math.sin(lat2);

        double lineDistance =
                Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
        double realDistance = Earth_Radius * Math.PI * 2 * Math.asin(0.5 * lineDistance) / 180;
        return realDistance;
    }

    public static void main(String[] args) {
    	// 深圳 113.88308, 22.55329， 广州 113.27324, 23.15792 ，清远 113.06269, 23.69795
    	//星河coco city 114.053267,22.608365  书香门第上河坊114.053777,22.603677
        System.out.println(DistanceHepler.distance(114.053267,22.608365, 114.053777,22.603677));
    }
}
