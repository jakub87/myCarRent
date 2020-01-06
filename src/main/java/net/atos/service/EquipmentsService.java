package net.atos.service;


import net.atos.model.Car;
import net.atos.model.Equipments;
import net.atos.model.dto.EquipmentsDto;
import net.atos.repository.CarRepository;
import net.atos.repository.EquipmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentsService {


    private EquipmentsRepository equipmentsRepository;
    private CarRepository carRepository;

    @Autowired
    public EquipmentsService(EquipmentsRepository equipmentsRepository, CarRepository carRepository) {
        this.equipmentsRepository = equipmentsRepository;
        this.carRepository = carRepository;
    }

    private boolean checkIsEquipmentExists(EquipmentsDto equipmentsDto) {
        return equipmentsRepository.existsByEquipmentDescription(equipmentsDto.getEquipmentDescription());

    }


    //dodanie nowego wyposazenia
    public boolean addCarEquipment(EquipmentsDto equipmentsDto) {
        if (checkIsEquipmentExists(equipmentsDto)) {
            return false;
        } else {
            Equipments carEquipments = new Equipments(equipmentsDto.getEquipmentDescription());
            equipmentsRepository.save(carEquipments);
            return true;
        }
        //Equipments carEquipments = new Equipments(equipmentsDto.getEquipmentDescription());
        //return equipmentsRepository.save(carEquipments);


    }

    //usuniecie wyposaenia
    public Equipments removeCarEquipment(Long carEquipments_id) {
        Equipments carEquipments = equipmentsRepository.getOne(carEquipments_id);
        equipmentsRepository.delete(carEquipments);
        return carEquipments;
    }



    public Equipments removeEquipmentFromCar(Long car_id,Long equipment_id) {
        Equipments equipments = equipmentsRepository.getOne(equipment_id);
        Car car = carRepository.getOne(car_id);
        car.removeEquipment(equipments);
        carRepository.save(car);
        return equipments;
    }

    //edycja pojazdu, dodanie wyposazenia do pojazdu
    public Equipments addEquipmentFromCar(Long car_id, Long equipment_id) {
        Equipments equipments = equipmentsRepository.getOne(equipment_id);
        Car car = carRepository.getOne(car_id);

        car.addEquipment(equipments);
        carRepository.save(car);
        return equipments;

    }

    public List<Equipments> getAll()
    {
        return equipmentsRepository.findAll();
    }

    public List <Equipments> getListEquipments()
    {
        return equipmentsRepository.findAll();
    }


    public Page<Equipments> getAllCarEquipments(Pageable pageable) {
        Page <Equipments> equipmentsList = equipmentsRepository.findAll(pageable);
        return equipmentsList;
    }
}
