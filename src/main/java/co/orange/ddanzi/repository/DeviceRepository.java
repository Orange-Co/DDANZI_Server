package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.user.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
}
