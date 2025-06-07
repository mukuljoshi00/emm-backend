package com.noviro.emm_backend.qr;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class QRRequest {
    private Object data;
    private int width = 300;
    private int height = 300;
}
