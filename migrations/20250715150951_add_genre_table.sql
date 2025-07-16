-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Genre] (
    [Id]        UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_Genre           PRIMARY KEY,
    [Name]      NVARCHAR(50)     NOT NULL CONSTRAINT UQ_Genre_Name      UNIQUE,
    [IsDeleted] BIT              NOT NULL CONSTRAINT DF_Genre_IsDeleted DEFAULT 0
);
-- +goose StatementEnd


-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Genre];
-- +goose StatementEnd
