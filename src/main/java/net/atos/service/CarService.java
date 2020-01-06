package net.atos.service;

import net.atos.model.Car;

import net.atos.model.CarPicture;
import net.atos.model.Equipments;
import net.atos.model.dto.CarDto;
import net.atos.model.dto.CarPictureDto;
import net.atos.model.dto.EquipmentsDto;
import net.atos.model.enums.Status;

import net.atos.repository.CarPictureRepository;
import net.atos.repository.CarRepository;
import net.atos.repository.EquipmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class CarService
{
    private CarRepository carRepository;
    private EquipmentsRepository equipmentsRepository;
    private CarPictureRepository carPictureRepository;
    private final String CAR_PATH_NAME = "C:\\Users\\Jakub\\Desktop\\projects_java\\myCarRent\\src\\main\\resources\\static\\images\\car_id_";

    @Autowired
    public CarService(CarRepository carRepository, EquipmentsRepository equipmentsRepository, CarPictureRepository carPictureRepository) {
        this.carRepository = carRepository;
        this.equipmentsRepository = equipmentsRepository;
        this.carPictureRepository = carPictureRepository;
    }

    private boolean addImage(MultipartFile image, Car car) throws IOException {
        if (!image.isEmpty()) {
            if (image.getOriginalFilename().endsWith(".jpeg")  ||
                image.getOriginalFilename().endsWith(".jpg")   ||
                image.getOriginalFilename().endsWith(".png")) {
                if (!carPictureRepository.existsCarPictureByPathOfURL(CAR_PATH_NAME+car.getCar_id()+"\\"+image.getOriginalFilename())) {
                    convertToFile(CAR_PATH_NAME+car.getCar_id(),image);
                    CarPicture imageTemp = new CarPicture(CAR_PATH_NAME+car.getCar_id()+"\\"+image.getOriginalFilename(),car);
                    car.addImage(imageTemp);
                     return true;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    //zapisujemy zdjecie w katalogu pojazdu
    private void convertToFile(String pathName, MultipartFile file) throws IOException {
        File convertedFile = new File(pathName+"\\"+file.getOriginalFilename());
        convertedFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
    }

    //dodanie nowego pojazdu,
    public List<String> addNewCar(CarDto carDto) throws IOException {
        List <String> rejectedImages = new ArrayList<>();
        Car car = new Car(carDto.getMark(),carDto.getModel(),carDto.getType(),carDto.getYear(),carDto.getColor(),carDto.getPrice(),carDto.getHotOffer());
        carRepository.save(car);

        new File(CAR_PATH_NAME+car.getCar_id()).mkdir(); // tworzymy katalog

        //==========dodajemy zdjecia
        for (CarPictureDto pictureTemp: carDto.getPictures()) {
               if (!addImage(pictureTemp.getPathOfURL(), car)) {
                    rejectedImages.add(pictureTemp.getPathOfURL().getOriginalFilename());
               }
                carRepository.save(car);
        }

        //==========dodajemy wyposazenie do pojazdu
        List <Equipments> equipmentsList = new ArrayList<>();
        for (EquipmentsDto eq: carDto.getEquipments()) {
            Long eq_id = Long.parseLong(eq.getEquipmentDescription());
            equipmentsList.add(equipmentsRepository.getOne(eq_id));
        }

        car.setEquipments(equipmentsList);
        //zapisujemy
        carRepository.save(car);
        return rejectedImages;
    }

    //usuniecie pojazdu
    public Car deleteCar(Long id) {
        Car car = getCarById(id);

        //usuwam pliki zdjec
        if (!car.getPictures().isEmpty()) {
            for ( CarPicture image : car.getPictures()) {
                new File(image.getPathOfURL()).delete();
            }
        }

        //usuwam katalog
        new File(CAR_PATH_NAME+car.getCar_id()).delete();

        carRepository.delete(car);
        return car;
    }

    //edycja pojazdu ==========================================
    public boolean updateCar(Long car_id, CarDto carDto) throws IOException {
        Car car = carRepository.getOne(car_id);

        //modyfikacja danych w formularzu
        car.setMark(carDto.getMark());
        car.setModel(carDto.getModel());
        car.setPrice(carDto.getPrice());
        car.setYear(carDto.getYear());
        car.setColor(carDto.getColor());
        car.setHotOffer(carDto.getHotOffer());
        car.setStatus(carDto.getStatus());
        car.setType(carDto.getType());

        //dodanie wyposazenia
        Optional<List<EquipmentsDto>> optionalEquipmentsDtoList = Optional.ofNullable(carDto.getEquipments());

        if (optionalEquipmentsDtoList.isPresent()) {
            for (EquipmentsDto equipmentsDto : optionalEquipmentsDtoList.get()) {
                Long equipments_id = Long.valueOf(equipmentsDto.getEquipmentDescription());
                car.addEquipment(equipmentsRepository.getOne(equipments_id));
            }
        }

        //dodajemy zdjecie
        boolean result = addImage(carDto.getImageFile(),car);
        carRepository.save(car);
        return result;
    }

    //lista wszystkich pojazdow
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    //wyszukiwanie z filtrowaniem pageable
    public Page<Car> getFilteredCars(String mark, String model, int yearFrom, int yearTo, BigDecimal priceFrom, BigDecimal priceTo,Pageable pageable) {
        return carRepository.searchCarsByFilters( mark,  model,  yearFrom,  yearTo,  priceFrom,  priceTo, pageable);
    }

    public void updateCarStatus(Long id, Status status) {
        Car car = carRepository.getOne(id);
        car.setStatus(status);
        carRepository.save(car);
    }

    public Page <Car> getHotOffers(Pageable pageable) {
        return carRepository.findCarsWithSpecialOffers(pageable);
    }

    public Car getCarById(Long id) {
        return carRepository.getOne(id);
    }

    public PagedListHolder <Equipments> getCarEquipments(Car car) {
       List <Equipments> carEquipmentsList = car.getEquipments();
       PagedListHolder <Equipments> equipmentsPage = new PagedListHolder<>(carEquipmentsList);
       return equipmentsPage;
    }
}