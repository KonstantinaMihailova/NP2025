package sensorAnalysis;

import javax.print.DocFlavor;
import java.util.*;
import java.util.stream.Collectors;

class BadSensorException extends Exception{
    public BadSensorException(String sensorId) {
        super(String.format("No readings for sensor: %s",sensorId));
    }
}

class BadMeasureException extends Exception{

    public BadMeasureException(int timestamp, String sensorId) {

        super(String.format("Error in timestamp: %d from sensor: %s",timestamp,sensorId));
    }
}

interface IGeo{
    double getLatitude();
    double getLongitude();
    default double distance(IGeo other) {
        return Math.sqrt(Math.pow(this.getLatitude() - other.getLatitude(), 2)
                + Math.pow(this.getLongitude() - other.getLongitude(), 2));
    }

}

class Measurement{
    int timestamp;
    float value;

    public Measurement(int timestamp, float value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public static Measurement createMeasurement(String data, String sensorId) throws BadMeasureException {
        String [] parts=data.split(":");
        int timestamp=Integer.parseInt(parts[0]);
        float value=Float.parseFloat(parts[1]);

        if(value<0)
            throw new BadMeasureException(timestamp,sensorId);

        return new Measurement(timestamp,value);
    }
}
class Sensor{
    String id;
    IGeo location;
    List<Measurement> measurements;

    public Sensor(String id, IGeo location, List<Measurement> measurements) {
        this.id = id;
        this.location = location;
        this.measurements = measurements;
    }

    public double getAverageValues(){
        return measurements.stream().mapToDouble(i->i.value).average().orElse(0);
    }

    public static Sensor createSensor(String line) throws BadMeasureException, BadSensorException {
        String [] parts=line.split("\\s+");

        String sensorId=parts[0];

        if(parts.length==3)
            throw new BadSensorException(sensorId);

        IGeo location=new IGeo() {
            @Override
            public double getLatitude() {
                return Double.parseDouble(parts[1]);
            }

            @Override
            public double getLongitude() {
                return Double.parseDouble(parts[2]);
            }
        };

        List<Measurement> measurements1 = new ArrayList<>();
        long toSkip = 3;
        for (String i : parts) {
            if (toSkip > 0) {
                toSkip--;
                continue;
            }
            Measurement measurement = Measurement.createMeasurement(i, sensorId);
            measurements1.add(measurement);
        }

        return new Sensor(sensorId,location,measurements1);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "sensorId='" + id + '\'' +
                '}';
    }

    public boolean hasMeasurementInThatTime(long from,long to){
        return measurements.stream().map(i->i.timestamp).anyMatch(t->t>=from && t<to);
    }
}
class GeoSensorApplication{
    List<Sensor> sensors;

    public GeoSensorApplication() {
        sensors=new ArrayList<>();
    }

    public void readGeoSensors(Scanner scanner) throws BadSensorException,BadMeasureException{

        while (scanner.hasNextLine()){
            String line=scanner.nextLine();
            sensors.add(Sensor.createSensor(line));
        }
    }

    public List<Sensor> inRange(IGeo location, double distance) {
        return sensors.stream()
                .filter(sensor -> sensor.location.distance(location)<distance)
                .collect(Collectors.toList());
    }

    public double averageValue(){
        return sensors.stream().mapToDouble(Sensor::getAverageValues).average().orElse(0);
    }

    public double averageDistanceValue (IGeo locationn,double distance, long from,long to){
        List<Sensor> matchingSensors=sensors.stream().filter(sensor -> sensor.hasMeasurementInThatTime(from,to)).collect(Collectors.toList());
        List<Sensor> finalSensors=matchingSensors.stream().filter(sensor -> sensor.location.distance(locationn)>=distance).collect(Collectors.toList());

        return finalSensors.stream().mapToDouble(sensor-> sensor.getAverageValues()).average().orElse(0);
    }

    public List<ExtremeValue> extremeValues(long timeFrom, long timeTo){
        return null;
    }


}

class ExtremeValue{

}
public class SensorAnalysis {
    public static void main(String[] args) {
        GeoSensorApplication app = new GeoSensorApplication();

        Scanner s = new Scanner(System.in);
        double lat = s.nextDouble();
        double lon = s.nextDouble();
        double dis = s.nextDouble();
        long t1 = s.nextLong();
        long t2 = s.nextLong();

        s.nextLine();

        System.out.println("Access point on {" + lat + ", " + lon + "} distance:" + dis + " from:" + t1 + " - to:" + t2);

        try {
            app.readGeoSensors(s);


            System.out.println(app.inRange(new IGeo() {
                @Override
                public double getLatitude() {
                    return lat;
                }

                @Override
                public double getLongitude() {
                    return lon;
                }
            }, dis));

            System.out.println(app.averageValue());
            System.out.println(app.averageDistanceValue(new IGeo() {
                @Override
                public double getLatitude() {
                    return lat;
                }

                @Override
                public double getLongitude() {
                    return lon;
                }
            }, dis, t1, t2));

            System.out.println(app.extremeValues(t1, t2));
        } catch (BadSensorException e) {
            System.out.println(e.getMessage());
        } catch (BadMeasureException e) {
            System.out.println(e.getMessage());
        } finally {
            s.close();
        }
    }
}