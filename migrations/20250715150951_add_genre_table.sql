-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Genre] (
    [Id] UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    [Name] NVARCHAR(50) NOT NULL UNIQUE,
    [IsDeleted] BIT NOT NULL DEFAULT 0
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Genre];
-- +goose StatementEnd
