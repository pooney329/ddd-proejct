CREATE TABLE wallet (
                        user_id      BIGINT      NOT NULL COMMENT '회원 ID',
                        balance      BIGINT      NOT NULL COMMENT '보유 잔액 (최소 0)',
                        version      BIGINT      NOT NULL DEFAULT 0 COMMENT '낙관적 락 버전',
                        created_at   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                        CONSTRAINT pk_wallet PRIMARY KEY (user_id),
                        CONSTRAINT ck_wallet_balance CHECK (balance >= 0)
) ENGINE=InnoDB
COMMENT='회원별 지갑 (재화 보유)';


CREATE TABLE ledger (
                        ledger_id    BIGINT      NOT NULL AUTO_INCREMENT COMMENT '원장 ID',
                        user_id      BIGINT      NOT NULL COMMENT '회원 ID',
                        request_id   VARCHAR(64) NOT NULL COMMENT '멱등 보장용 요청 ID',
                        amount       BIGINT      NOT NULL COMMENT '변동 금액',
                        type         VARCHAR(20) NOT NULL COMMENT '변동 타입 (DEBIT, CREDIT)',
                        created_at   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT pk_ledger PRIMARY KEY (ledger_id),
                        CONSTRAINT uk_ledger_request UNIQUE (request_id),
                        CONSTRAINT ck_ledger_amount CHECK (amount > 0),
                        CONSTRAINT ck_ledger_type CHECK (type IN ('DEBIT','CREDIT')),
                        CONSTRAINT fk_ledger_wallet
                            FOREIGN KEY (user_id) REFERENCES wallet(user_id)
) ENGINE=InnoDB
COMMENT='재화 변동 원장 (멱등/감사)';


CREATE INDEX idx_ledger_user_created
    ON ledger (user_id, created_at);

CREATE INDEX idx_ledger_user_type
    ON ledger (user_id, type);
