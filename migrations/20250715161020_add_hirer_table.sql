-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Hirer] (
    [Id] UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    [Alias] NVARCHAR(50) NOT NULL,
    [Email] VARCHAR(50) UNIQUE NOT NULL,
    [Phone] VARCHAR(15) UNIQUE NOT NULL,
    [CreatedTime] DATETIME,
    [UpdatedTime] DATETIME
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Hirer];
-- +goose StatementEnd
