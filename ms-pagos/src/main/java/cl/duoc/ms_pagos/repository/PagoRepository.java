package cl.duoc.ms_pagos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.ms_pagos.model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByCuentaOrigenId(Long cuentaOrigenId);

    List<Pago> findByCuentaDestinoId(Long cuentaDestinoId);
}