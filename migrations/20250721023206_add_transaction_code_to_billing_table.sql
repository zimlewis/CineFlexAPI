-- +goose Up
-- +goose StatementBegin
ALTER TABLE [dbo].[BillingDetail] 
ADD [TransactionCode] VARCHAR(10) NOT NULL CONSTRAINT UQ_BillingDetail_TransactionCode UNIQUE;
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
ALTER TABLE [dbo].[BillingDetail] 
DROP CONSTRAINT UQ_BillingDetail_TransactionCode;

ALTER TABLE [dbo].[BillingDetail]
DROP COLUMN [TransactionCode]
-- +goose StatementEnd
