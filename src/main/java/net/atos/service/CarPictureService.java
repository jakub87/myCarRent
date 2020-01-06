package net.atos.service;

import net.atos.model.CarPicture;
import net.atos.repository.CarPictureRepository;
import net.atos.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class CarPictureService
{
    private CarPictureRepository carPictureRepository;
    private CarRepository carRepository;

    @Autowired
    public CarPictureService(CarPictureRepository carPictureRepository, CarRepository carRepository) {
        this.carPictureRepository = carPictureRepository;
        this.carRepository = carRepository;
    }

    public CarPicture deleteImageFromCar(Long carPicture_id) {
        CarPicture carPicture = carPictureRepository.getOne(carPicture_id);
        new File(carPicture.getPathOfURL()).delete();
        carPictureRepository.delete(carPicture);
        return carPicture;
    }

}
